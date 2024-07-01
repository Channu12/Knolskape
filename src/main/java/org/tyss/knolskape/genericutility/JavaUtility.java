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

	public boolean compareTwoImagesWithoutCOnfidenceScore(String image1Path, String image2Path, String differenceFolderDirectory) throws IOException {
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
			boolean differencesFound = false;

			// Compare images pixel by pixel
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int rgb1 = image1.getRGB(x, y);
					int rgb2 = image2.getRGB(x, y);

					if (rgb1 != rgb2) {
						differencesFound = true;
						// Highlight the difference in red color
						diffImage.setRGB(x, y, Color.RED.getRGB());
					} else {
						// Keep the pixel from image1 in the difference image
						diffImage.setRGB(x, y, rgb1);
					}
				}
			}

			if (!differencesFound) {
				System.out.println("No differences found between the images.");
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