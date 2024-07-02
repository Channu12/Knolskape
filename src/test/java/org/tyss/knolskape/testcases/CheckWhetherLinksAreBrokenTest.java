package org.tyss.knolskape.testcases;

import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;

public class CheckWhetherLinksAreBrokenTest extends BaseClass {

	@Test
	public void checkWhetherLinksAreBroken() {
		testCaseId = "KN-T2";

		// Welcome Page Links Verification
		webDriverUtility.checkForBrokenLinks(driver);

		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(6);
		
		// Home Page Links Verification
		webDriverUtility.checkForBrokenLinks(driver);
	}
}
