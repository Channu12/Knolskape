package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;
import org.tyss.knolskape.genericutility.UtilityObjectClass;
import org.tyss.knolskape.objectrepository.HomePage;
import org.tyss.knolskape.objectrepository.WelcomePage;

@Listeners(org.tyss.knolskape.genericutility.ListenerImplementationClass.class)
public class CheckTheExpectedElementsArePresentInGuiTest extends BaseClass {
	@Test
	public void checkTheExpectedElementsArePresentInGuiTest() throws EncryptedDocumentException, IOException {
		UtilityObjectClass.setTestCaseId("KN-T4");
		String expectedTextsOfElements;
		String actualTextsOfElements;

		// Create Object for Welcome Page
		WelcomePage welcomePage = new WelcomePage(driver);

		UtilityObjectClass.getExtentTest().info("*******************Welcome Page********************");
		// Welcome Page Elements Verification
		expectedTextsOfElements = excelUtility.getDataFromExcelCellBasedOnUniqueDataAndHeader(IConstants.EXCEL_FILE_PATH, "Sheet1", clientName, "WelcomePage Elements");
		actualTextsOfElements = webDriverUtility.getTextFromListOfWebElementsInString(welcomePage.getListOfStyledText());
		assertionUtility.assertTwoStrings(expectedTextsOfElements, actualTextsOfElements);

		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(6);

		// Create Object for Home Page
		HomePage homePage = new HomePage(driver);

		UtilityObjectClass.getExtentTest().info("*******************Home Page********************");
		// Home Page Elements Verification
		expectedTextsOfElements = excelUtility.getDataFromExcelCellBasedOnUniqueDataAndHeader(IConstants.EXCEL_FILE_PATH, "Sheet1", clientName, "HomePage Elements");
		actualTextsOfElements = webDriverUtility.getTextFromListOfWebElementsInString(homePage.getListOfStyledText());
		assertionUtility.assertTwoStrings(expectedTextsOfElements, actualTextsOfElements);

	}
}
