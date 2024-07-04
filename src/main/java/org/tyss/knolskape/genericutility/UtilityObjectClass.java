package org.tyss.knolskape.genericutility;

import org.openqa.selenium.WebDriver;
import org.testng.xml.XmlTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class UtilityObjectClass {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<XmlTest> xmlTest = new ThreadLocal<XmlTest>();
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<ExtentReports> extentReports = new ThreadLocal<ExtentReports>();
	private static ThreadLocal<String> clientName = new ThreadLocal<String>();
	private static ThreadLocal<String> cycleId = new ThreadLocal<String>();
	private static ThreadLocal<String> browserName = new ThreadLocal<String>();
	private static ThreadLocal<String> testCaseId = new ThreadLocal<String>();
	
	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void setDriver(WebDriver actDriver) {
		driver.set(actDriver);
	}

	public static XmlTest getXmlTest() {
		return xmlTest.get();
	}

	public static void setXmlTest(XmlTest actXmlTest) {
		xmlTest.set(actXmlTest);;
	}

	public static ExtentTest getExtentTest() {
		return extentTest.get();
	}

	public static void setExtentTest(ExtentTest actExtentTest) {
		extentTest.set(actExtentTest);
	}

	public static ExtentReports getExtentReports() {
		return extentReports.get();
	}

	public static void setExtentReports(ExtentReports actExtentReports) {
		extentReports.set(actExtentReports);
	}

	public static String getClientName() {
		return clientName.get();
	}

	public static void setClientName(String actClientName) {
		clientName.set(actClientName);
	}

	public static String getCycleId() {
		return cycleId.get();
	}

	public static void setCycleId(String actCycleId) {
		cycleId.set(actCycleId);
	}
	
	public static String getBrowserName() {
		return browserName.get();
	}

	public static void setBrowserName(String actBrowserName) {
		browserName.set(actBrowserName);
	}
	
	public static String getTestCaseId() {
		return testCaseId.get();
	}

	public static void setTestCaseId(String actTestCaseId) {
		testCaseId.set(actTestCaseId);
	}
}
