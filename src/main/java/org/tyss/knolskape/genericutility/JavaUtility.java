package org.tyss.knolskape.genericutility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class JavaUtility {

	public String getDateAndTimeInSpecifiedFormat(String format) {
		return new SimpleDateFormat(format).format(new Date()).toString();
	}

	public int getRandomNumber(int range) {
		return new Random().nextInt(range);
	}

	public String getCurrentProjectDirectory() {
		return System.getProperty("user.dir");
	}

	// function to generate a random string of length n
	public String getRandomAlphaNumericString(int n) {
		// choose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public Boolean compareTwoImagesWithoutCcnfidenceScore(String imagePath1, String imagePath2, String differenceFolderDirectory, int confidenceScore) {
		Boolean flag = false;

		// Load the OpenCV library
		System.load(System.getProperty("user.dir")+"\\src\\test\\resources\\opencv_java490.dll");

		// Load the images
		Mat img1 = Imgcodecs.imread(imagePath1);
		Mat img2 = Imgcodecs.imread(imagePath2);

		//		System.out.println( img1.size());        
		// Resize images to the same resolution
		Mat resizedImg1 = new Mat();
		Mat resizedImg2 = new Mat();
		Size size = new Size( img1.size().width,  img1.size().height); // Adjust size as necessary
		Imgproc.resize(img1, resizedImg1, size);
		Imgproc.resize(img2, resizedImg2, size);

		// Convert images to grayscale
		Mat grayImg1 = new Mat();
		Mat grayImg2 = new Mat();
		Imgproc.cvtColor(resizedImg1, grayImg1, Imgproc.COLOR_BGR2GRAY);
		Imgproc.cvtColor(resizedImg2, grayImg2, Imgproc.COLOR_BGR2GRAY);

		// Calculate the absolute difference between images
		Mat diff = new Mat();
		Core.absdiff(grayImg1, grayImg2, diff);
		diff.convertTo(diff, CvType.CV_32F);
		diff = diff.mul(diff);

		// Calculate mean and standard deviation for SSIM
		MatOfDouble mean = new MatOfDouble();
		MatOfDouble stddev = new MatOfDouble();
		Core.meanStdDev(diff, mean, stddev);
		double ssim = mean.get(0, 0)[0] + stddev.get(0, 0)[0];

		double matchPercentage = 100 - (ssim / (grayImg1.rows() * grayImg1.cols())*100);
		if (matchPercentage>=confidenceScore) {
			flag = true;
			UtilityObjectClass.getExtentTest().pass("["+imagePath1+"] and ["+imagePath2+"] Images are "+matchPercentage+"% Similar.");
			System.out.println("["+imagePath1+"] and ["+imagePath2+"] Images are "+matchPercentage+"% Similar.");
		} else {
			UtilityObjectClass.getExtentTest().fail("There is a difference between both images. Similarity Percentage: "+matchPercentage);
			System.err.println("There is a difference between both images. Similarity Percentage: "+matchPercentage);

			// Normalize the difference for visualization
			Mat normalizedDiff = new Mat();
			Core.normalize(diff, normalizedDiff, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

			// Threshold the normalized difference to create a binary mask
			Mat mask = new Mat();
			Imgproc.threshold(normalizedDiff, mask, 30, 255, Imgproc.THRESH_BINARY);

			// Convert the original image to a 4-channel image for highlighting
			Mat originalColored = new Mat();
			Imgproc.cvtColor(resizedImg1, originalColored, Imgproc.COLOR_BGR2BGRA);

			// Create a color image to highlight differences in pink
			Mat highlightedDiff = new Mat(originalColored.size(), CvType.CV_8UC4, new Scalar(255, 0, 255, 255));
			
			// Use the mask to blend the highlighted differences with the original image
			for (int y = 0; y < mask.rows(); y++) {
				for (int x = 0; x < mask.cols(); x++) {
					if (mask.get(y, x)[0] > 0) {
						double[] rgba = originalColored.get(y, x);
						rgba[0] = 255; // Blue channel
						rgba[1] = 0;   // Green channel
						rgba[2] = 255; // Red channel
						rgba[3] = 255; // Alpha channel
						originalColored.put(y, x, rgba);
					}
				}
			}

			// Create output folder with timestamp
			String outputPath = differenceFolderDirectory + File.separator + "Difference_Image_" + getDateAndTimeInSpecifiedFormat("yyyyMMdd_HHmmss") + ".png";
			File outputFolderFile = new File(differenceFolderDirectory);
			if (!outputFolderFile.exists()) {
				outputFolderFile.mkdirs(); // Create output folder if it doesn't exist
			}

			// Save the highlighted difference image
			Imgcodecs.imwrite(outputPath, originalColored);
			UtilityObjectClass.getExtentTest().info("Difference image is stored at: "+outputPath);
			System.out.println("Difference image is stored at: "+outputPath);
		}
		return flag;
	}
}