package org.tyss.knolskape.genericutility;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebDriverUtility {
	JavaUtility javaUtility = new JavaUtility();

	public void enterInputIntoElement(WebDriver driver, String input, WebElement element) {
		element.sendKeys(input);
	}

	public void clickOnELement(WebDriver driver ,WebElement element) {
		element.click();
	}

	public void waitForSeconds(int seconds) {
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String getScreenshotOfCurrentPage(WebDriver driver, String fileName, String directory) {
		// Ensure the directory exists
		try {
			Files.createDirectories(Paths.get(directory));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Take the screenshot
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Define the destination file
		File destinationFile = new File(directory, fileName + "_" + javaUtility.getDateAndTimeInSpecifiedFormat("yyyyMMdd_HHmmss") + ".png");

		// Copy the screenshot to the destination
		try {
			Files.copy(screenshot.toPath(), destinationFile.toPath());
			System.out.println("Screenshot saved to: " + destinationFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destinationFile.getAbsolutePath();
	}

	public void checkForBrokenLinks(WebDriver driver) {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		//		links.addAll(driver.findElements(By.tagName("img")));

		System.out.println("Total links on the page: " + links.size());

		for (WebElement element : links) {
			String url = element.getAttribute("href");
			if (url == null || url.isEmpty()) {
				System.out.println("URL is either not configured for anchor tag or it is empty");
				continue;
			}
			
			try {
				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setRequestMethod("HEAD");
				connection.connect();
				int respCode = connection.getResponseCode();
				if (respCode >= 400) {
					System.out.println(url + " is a broken link");
				} else {
					System.out.println(url + " is a valid link");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}