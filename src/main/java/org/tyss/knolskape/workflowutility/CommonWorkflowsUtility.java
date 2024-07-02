package org.tyss.knolskape.workflowutility;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.tyss.knolskape.genericutility.FileUtility;
import org.tyss.knolskape.genericutility.JavaUtility;
import org.tyss.knolskape.genericutility.RestAssuredUtility;
import org.tyss.knolskape.genericutility.WebDriverUtility;
import org.tyss.knolskape.objectrepository.WelcomePage;

public class CommonWorkflowsUtility {
	FileUtility fileUtility = new FileUtility();
	JavaUtility javaUtility = new JavaUtility();
	RestAssuredUtility restAssuredUtility = new RestAssuredUtility();
	WebDriverUtility webDriverUtility = new WebDriverUtility();

	public void signInToApplication(WebDriver driver, String email, String password) {
		WelcomePage welcomePage = new WelcomePage(driver);
		webDriverUtility.enterInputIntoElement(driver, email, welcomePage.getEmailIdTextfield());
		webDriverUtility.enterInputIntoElement(driver, password, welcomePage.getPasswordTextfield());
		webDriverUtility.clickOnELement(driver, welcomePage.getSignInButton());
	}
	
	public boolean getScreenshotOfCurrentPageAndCompareWithGoldenImage(WebDriver driver, String pageName, String appName, double confidenceScore) throws IOException {
		// Create resources folder path
		String resourcesFolderPath = javaUtility.getCurrentProjectDirectory()+"\\src\\test\\resources";

		// Get screenshot of current page
		String actualWelcomePagePath = webDriverUtility.getScreenshotOfCurrentPage(driver, pageName, resourcesFolderPath+"\\ActualImages\\"+appName);
		
		// Compare screenshot with golden image
		boolean isPagesAreSame = javaUtility.compareTwoImagesWithoutCOnfidenceScore(actualWelcomePagePath, resourcesFolderPath+"\\GoldenImages\\"+appName+"\\"+pageName+".png", resourcesFolderPath+"\\DifferenceImages\\"+appName, confidenceScore);
		return isPagesAreSame;
	}
}

