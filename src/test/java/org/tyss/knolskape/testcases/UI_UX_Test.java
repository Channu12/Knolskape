package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;

public class UI_UX_Test extends BaseClass {

	@Test
	public void ui_ux_Test() throws IOException {
		testCaseId = "KN-T1";
		boolean isPagesAreSame = false;
		
		// Welcome Page UI Verification
		isPagesAreSame = commonWorkflowsUtility.getScreenshotOfCurrentPageAndCompareWithGoldenImage(driver, "WelcomePage", clientName, 0.75);
		Assert.assertEquals(isPagesAreSame, true);
		
		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(5);

		// Home Page UI Verification
		isPagesAreSame = commonWorkflowsUtility.getScreenshotOfCurrentPageAndCompareWithGoldenImage(driver, "HomePage", clientName, 0.75);
		Assert.assertEquals(isPagesAreSame, true);
	}
}
