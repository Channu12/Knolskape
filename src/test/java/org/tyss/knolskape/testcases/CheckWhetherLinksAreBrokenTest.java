package org.tyss.knolskape.testcases;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.tyss.knolskape.genericutility.BaseClass;
import org.tyss.knolskape.genericutility.IConstants;
import org.tyss.knolskape.genericutility.UtilityObjectClass;

@Listeners(org.tyss.knolskape.genericutility.ListenerImplementationClass.class)
public class CheckWhetherLinksAreBrokenTest extends BaseClass {

	@Test
	public void checkWhetherLinksAreBrokenTest() {
		UtilityObjectClass.setTestCaseId("KN-T5");
		boolean areBroken = false;

		UtilityObjectClass.getExtentTest().info("*******************Welcome Page********************");
		// Welcome Page Links Verification
		areBroken = webDriverUtility.checkForBrokenLinks(driver);
		assertionUtility.assertBooleanValue(areBroken);

		// SignIn to application
		String email = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_EMAIL");
		String password = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_PASSWORD");
		commonWorkflowsUtility.signInToApplication(driver, email, password);
		webDriverUtility.waitForSeconds(6);

		UtilityObjectClass.getExtentTest().info("*******************Home Page********************");
		// Home Page Links Verification
		areBroken = webDriverUtility.checkForBrokenLinks(driver);
		assertionUtility.assertBooleanValue(areBroken);
	}
}
