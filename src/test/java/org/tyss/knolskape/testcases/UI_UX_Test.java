package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;
import org.tyss.knolskape.genericutility.UtilityObjectClass;

@Listeners(org.tyss.knolskape.genericutility.ListenerImplementationClass.class)
public class UI_UX_Test extends BaseClass {
	@Test
	public void ui_ux_Test() throws IOException {
		UtilityObjectClass.setTestCaseId("KN-T6");
		boolean isSame = false;

		UtilityObjectClass.getExtentTest().info("*******************Welcome Page********************");
		// Welcome Page UI Verification
		isSame=commonWorkflowsUtility.compareCurrentPageScreenshotWithGoldenIamge(driver, "WelcomePage", clientName, 75);
		assertionUtility.assertBooleanValue(isSame);

		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(6);

		UtilityObjectClass.getExtentTest().info("*******************Home Page********************");
		// Home Page UI Verification
		isSame = commonWorkflowsUtility.compareCurrentPageScreenshotWithGoldenIamge(driver, "HomePage", clientName, 75);
		assertionUtility.assertBooleanValue(isSame);
	}
}
