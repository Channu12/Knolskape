package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;
import org.tyss.knolskape.objectrepository.HomePage;
import org.tyss.knolskape.objectrepository.WelcomePage;

public class CheckTheExpectedElementsArePresentInUITest extends BaseClass {

	@Test
	public void checkTheExpectedElementsArePresentInUI() throws EncryptedDocumentException, IOException {
		testCaseId = "KN-T3";
		String expectedElements;
		String actualElements;

		// Create Object for Welcome Page
		WelcomePage welcomePage = new WelcomePage(driver);

		// Welcome Page Elements Verification
		expectedElements = excelUtility.getDataFromExcelCellBasedOnUniqueDataAndHeader(IConstants.EXCEL_FILE_PATH, "Sheet1", clientName, "WelcomePage Elements");
		actualElements = webDriverUtility.getTextFromListOfWebElementsInString(welcomePage.getListOfStyledText());
		Assert.assertEquals(expectedElements, actualElements);
		
		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(5);
		
		// Create Object for Home Page
		HomePage homePage = new HomePage(driver);

		// Welcome Page Elements Verification
		expectedElements = excelUtility.getDataFromExcelCellBasedOnUniqueDataAndHeader(IConstants.EXCEL_FILE_PATH, "Sheet1", clientName, "HomePage Elements");
		actualElements = webDriverUtility.getTextFromListOfWebElementsInString(homePage.getListOfStyledText());
		Assert.assertEquals(expectedElements, actualElements);
	}
}