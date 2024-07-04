package org.tyss.knolskape.genericutility;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.xml.XmlTest;
import org.tyss.knolskape.workflowutility.CommonWorkflowsUtility;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass {
	public ExcelUtility excelUtility = new ExcelUtility();
	public FileUtility fileUtility = new FileUtility();
	public JavaUtility javaUtility = new JavaUtility();
	public JiraUtility jiraUtility = new JiraUtility();
	public AssertionUtility assertionUtility = new AssertionUtility();
	public RestAssuredUtility restAssuredUtility = new RestAssuredUtility();
	public WebDriverUtility webDriverUtility = new WebDriverUtility();
	public CommonWorkflowsUtility commonWorkflowsUtility = new CommonWorkflowsUtility();
	public WebDriver driver;
	public String clientName;
	String cycleId;
	String browserName;

	@BeforeSuite
	public void configBS(XmlTest config) {
		UtilityObjectClass.setXmlTest(config);
		clientName = System.getProperty("ClientName", config.getParameter("ClientName"));
		UtilityObjectClass.setClientName(clientName);
		cycleId = jiraUtility.createTestCycle(clientName+"_"+javaUtility.getDateAndTimeInSpecifiedFormat("yyyyMMdd_HHmmss"));
		UtilityObjectClass.setCycleId(cycleId);
	}

	@BeforeClass
	public void configBC() {
		System.out.println("*********Open Browser*********");
		clientName = UtilityObjectClass.getClientName();
		String url = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_URL");
		XmlTest config = UtilityObjectClass.getXmlTest();
		String browserName = System.getProperty("BrowserName", config.getParameter("BrowserName"));
		UtilityObjectClass.setBrowserName(browserName);
		if (browserName.trim().equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if(browserName.trim().equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if(browserName.trim().equalsIgnoreCase("edge")){
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		UtilityObjectClass.setDriver(driver);
		webDriverUtility.maximizeBrowserWindow(driver);
		webDriverUtility.navigateToUrl(driver, url);
		webDriverUtility.implicitWaitForSeconds(driver, IConstants.IMPLICIT_WAIT_TIME);
	}

	@BeforeMethod
	public void configBM(ITestResult result) {
		System.out.println("Test Case: "+result.getMethod().getMethodName()+" execution has been triggered.");
		//		System.out.println("*********SignIn to the Application*********");
		//		commonFlowUtility.loginToApplication(driver, email, password);
	}

	@AfterMethod
	public void configAM(ITestResult result) {
		//		System.out.println("*********Sign out from the Application*********");
		cycleId = UtilityObjectClass.getCycleId();
		String testCaseId = UtilityObjectClass.getTestCaseId();
		if (result.getStatus() == ITestResult.SUCCESS) {
			jiraUtility.addTestCaseToCycleAndUpdateResults(cycleId, testCaseId, "Pass");
		} else if (result.getStatus() == ITestResult.FAILURE) {
			jiraUtility.addTestCaseToCycleAndUpdateResults(cycleId, testCaseId, "Fail");
		} else if (result.getStatus() == ITestResult.SKIP) {
			jiraUtility.addTestCaseToCycleAndUpdateResults(cycleId, testCaseId, "Skip");
		}
	}

	@AfterClass
	public void configAC() {
		System.out.println("*********Close Browser*********\r\n");
		try {
			driver.quit();
		} catch (Exception e) {
		}
	}
}
