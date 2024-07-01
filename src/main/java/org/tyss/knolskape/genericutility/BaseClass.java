package org.tyss.knolskape.genericutility;

import java.time.Duration;

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
	public RestAssuredUtility restAssuredUtility = new RestAssuredUtility();
	public WebDriverUtility webDriverUtility = new WebDriverUtility();
	public CommonWorkflowsUtility commonWorkflowsUtility = new CommonWorkflowsUtility();
	public WebDriver driver;
	public String clientName = "";
	public String testCaseId;
	String cycleId;
	
	@BeforeSuite
	public void configBS(XmlTest config) {
		clientName = System.getProperty("ClientName", config.getParameter("ClientName"));
		cycleId = jiraUtility.createTestCycle(clientName+javaUtility.getDateAndTimeInSpecifiedFormat("yyyyMMdd_HHmmss"));
	}
	
	@BeforeClass
	public void configBC() {
		System.out.println("*********Open Browser*********");
		String browserName = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, "BROWSER_NAME");
		String url = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, clientName+"_URL");
		if (browserName.trim().equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} else if(browserName.trim().equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} else if(browserName.trim().equalsIgnoreCase("edge")){
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			driver.get(url);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		} else {
			System.out.println("Invalid Browser Name given. please try again");
		}
	}

	@BeforeMethod
	public void configBM() {
		//		System.out.println("*********SignIn to the Application*********");
		//		commonFlowUtility.loginToApplication(driver, email, password);
	}

	@AfterMethod
	public void configAM(ITestResult result) {
		//		System.out.println("*********Sign from the Application*********");
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
		System.out.println("*********Close Browser*********");
		driver.quit();
	}
}
