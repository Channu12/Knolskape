package org.tyss.knolskape.genericutility;

import org.openqa.selenium.WebDriver;
import org.testng.xml.XmlTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class UtilityObjectClass {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();
	private static ThreadLocal<XmlTest> config = new ThreadLocal<XmlTest>();

	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<ExtentTest> nodeTest = new ThreadLocal<ExtentTest>();
	private static ThreadLocal<ExtentReports> report = new ThreadLocal<ExtentReports>();

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static void setDriver(WebDriver actDriver) {
		driver.set(actDriver);
	}

	public static XmlTest getConfig() {
		return config.get();
	}

	public static void setConfig(XmlTest actConfig) {
		config.set(actConfig);
	}

	public static ExtentReports getReport() {
		return report.get();
	}

	public static void setReport(ExtentReports reports) {
		report.set(reports);
	}

	public static ExtentTest getExtentTest() {
		return extentTest.get();
	}

	public static void setExtentTest(ExtentTest extent) {
		extentTest.set(extent);
	}
	
	public static ExtentTest getExtentNodeTest() {
		return nodeTest.get();
	}

	public static void setExtentNodeTest(ExtentTest extent) {
		nodeTest.set(extent);
	}
}
