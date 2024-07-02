package testingPackage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Test {
	public static void main(String[] args) {
		// Load the OpenCV library
		String userDir = System.getProperty("user.dir");
		System.load(userDir+"\\src\\test\\resources\\opencv_java490.dll");

		// File paths
		String imagePath1 = "C:\\Users\\User\\Desktop\\Comparison\\a.png";
		String imagePath2 = "C:\\Users\\User\\Desktop\\Comparison\\b.png";

		// Load the images
		Mat img1 = Imgcodecs.imread(imagePath1);
		Mat img2 = Imgcodecs.imread(imagePath2);

		System.out.println( img1.size());        
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

		System.out.println("SSIM: " + (1 - ssim / (grayImg1.rows() * grayImg1.cols())));

		double matchPercentage = 1 - ssim / (grayImg1.rows() * grayImg1.cols());
		if (matchPercentage==1) {
			System.out.println("Both Images are same.");
		} else {
			System.err.println("There is a difference between both images.");
		}
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
		
		// Save the highlighted difference image
		Imgcodecs.imwrite("C:\\Users\\User\\Desktop\\Comparison\\d.png", originalColored);
	}
}

