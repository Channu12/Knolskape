package org.tyss.knolskape.genericutility;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

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

	public boolean compareTwoImages(String image1Path, String image2Path, String differenceFolderDirectory, double confidenceThreshold) throws IOException {
		resizeImage(image1Path, image2Path);

		try {
			BufferedImage image1 = ImageIO.read(new File(image1Path));
			BufferedImage image2 = ImageIO.read(new File(image2Path));

			int width = image1.getWidth();
			int height = image1.getHeight();

			// Ensure the images are the same size
			if (width != image2.getWidth() || height != image2.getHeight()) {
				System.out.println("Images are not of the same size.");
				return false;
			}

			BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			int differingPixels = 0;
			int totalPixels = width * height;

			// Compare images pixel by pixel
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int rgb1 = image1.getRGB(x, y);
					int rgb2 = image2.getRGB(x, y);

					if (rgb1 != rgb2) {
						differingPixels++;
						// Highlight the difference in red color
						diffImage.setRGB(x, y, Color.RED.getRGB());
					} else {
						// Keep the pixel from image1 in the difference image
						diffImage.setRGB(x, y, rgb1);
					}
				}
			}

			double similarity = 1.0 - ((double) differingPixels / totalPixels);
			//	        System.out.println("Similarity: " + (similarity * 100) + "%");

			if (similarity >= confidenceThreshold) {
				System.out.println("Images match with confidence: " + (similarity * 100) + "%");
				return true;
			} else {
				// Create output folder with timestamp
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timestamp = sdf.format(new Date());
				String outputPath = differenceFolderDirectory + File.separator + "Difference_Image_" + timestamp + ".png";
				File outputFolderFile = new File(differenceFolderDirectory);
				if (!outputFolderFile.exists()) {
					outputFolderFile.mkdirs(); // Create output folder if it doesn't exist
				}

				// Save the difference image
				File diffFile = new File(outputPath);
				ImageIO.write(diffImage, "png", diffFile);

				System.out.println("Difference image saved successfully: " + outputPath);
				return false;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

//	public boolean compareTwoImagesWithoutCOnfidenceScore(String image1Path, String image2Path, String differenceFolderDirectory) throws IOException {
//		resizeImage(image1Path, image2Path);
//
//		try {
//			BufferedImage image1 = ImageIO.read(new File(image1Path));
//			BufferedImage image2 = ImageIO.read(new File(image2Path));
//
//			int width = image1.getWidth();
//			int height = image1.getHeight();
//
//			// Ensure the images are the same size
//			if (width != image2.getWidth() || height != image2.getHeight()) {
//				System.out.println("Images are not of the same size.");
//				return false;
//			}
//
//			BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//			boolean differencesFound = false;
//
//			// Compare images pixel by pixel
//			for (int y = 0; y < height; y++) {
//				for (int x = 0; x < width; x++) {
//					int rgb1 = image1.getRGB(x, y);
//					int rgb2 = image2.getRGB(x, y);
//
//					if (rgb1 != rgb2) {
//						differencesFound = true;
//						// Highlight the difference in red color
//						diffImage.setRGB(x, y, Color.RED.getRGB());
//					} else {
//						// Keep the pixel from image1 in the difference image
//						diffImage.setRGB(x, y, rgb1);
//					}
//				}
//			}
//			
//			if (!differencesFound) {
//				System.out.println("No differences found between the images.");
//				return true;
//			} else {
//				// Create output folder with timestamp
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//				String timestamp = sdf.format(new Date());
//				String outputPath = differenceFolderDirectory + File.separator + "Difference_Image_" + timestamp + ".png";
//				File outputFolderFile = new File(differenceFolderDirectory);
//				if (!outputFolderFile.exists()) {
//					outputFolderFile.mkdirs(); // Create output folder if it doesn't exist
//				}
//
//				// Save the difference image
//				File diffFile = new File(outputPath);
//				ImageIO.write(diffImage, "png", diffFile);
//
//				System.out.println("Difference image saved successfully: " + outputPath);
//				return false;
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

	
	
	public Boolean compareTwoImagesWithoutCOnfidenceScore(String imagePath1, String imagePath2, String differenceFolderDirectory, double confidenceScore) {

		Boolean flag = false;
		// Load the OpenCV library
		String userDir = System.getProperty("user.dir");
		System.load(userDir+"\\src\\test\\resources\\opencv_java490.dll");

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

		double matchPercentage = 1 - ssim / (grayImg1.rows() * grayImg1.cols());
		if (matchPercentage>=confidenceScore) {
			flag = true;
			System.out.println("Both Images are same.");
		} else {
			System.err.println("There is a difference between both images.");
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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String timestamp = sdf.format(new Date());
			String outputPath = differenceFolderDirectory + File.separator + "Difference_Image_" + timestamp + ".png";
			File outputFolderFile = new File(differenceFolderDirectory);
			if (!outputFolderFile.exists()) {
				outputFolderFile.mkdirs(); // Create output folder if it doesn't exist
			}
			
			// Save the highlighted difference image
			Imgcodecs.imwrite(outputPath, originalColored);
			System.out.println("Difference image is stored at: "+outputPath);
		}
		return flag;
	}
	
	
	
	
	
	private static void resizeImage(String imagePathToBeResized, String referenceImagePath) throws IOException {

		// Read the reference image from the file system
		File imageFile = new File(referenceImagePath);
		BufferedImage image = ImageIO.read(imageFile);

		// Retrieve the width and height of the reference image
		int targetWidth = image.getWidth();
		int targetHeight = image.getHeight();

		// Read the original image from the file system
		BufferedImage originalImage = ImageIO.read(new File(imagePathToBeResized));

		// Create a new resized image
		BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = resizedImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();

		// Extract the parent directory and file name from the original image path
		File inputFile = new File(imagePathToBeResized);
		File parentDirectory = inputFile.getParentFile();
		String outputFileName = inputFile.getName(); // Keep the original file name
		String outputImagePath = parentDirectory.getAbsolutePath() + File.separator + outputFileName;

		// Save the resized image to the same location as the original image
		File outputFile = new File(outputImagePath);
		ImageIO.write(resizedImage, "png", outputFile);
	}
}