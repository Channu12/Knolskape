package org.tyss.knolskape.genericutility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {
	private Workbook workbook;

	/**
	 * This method is used to initialize and open the excel workbook
	 * @param excelPath
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public void openExcel(String excelPath) throws EncryptedDocumentException, IOException {
		FileInputStream fisExcel = new FileInputStream(excelPath);
		workbook = WorkbookFactory.create(fisExcel);
	}

	/**
	 * This method is used to fetch the data from the excel based on key
	 * 
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 */
	public String getDataFromExcel(String excelPath, String sheetName, String testcaseName, String requiredKey)
			throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		boolean flag = false;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = sheet.getRow(i).getCell(0).getStringCellValue();
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(testcaseName)) {
				for (int j = 1; j <= sheet.getRow(i).getLastCellNum(); j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
					} catch (Exception e) {
					}
					if (actualKey.equalsIgnoreCase(requiredKey)) {
						try {
							value = df.formatCellValue(sheet.getRow(i).getCell(j), evaluator);
						} catch (Exception e) {
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		workbook.close();
		return value;
	}

	/**
	 * This method is used to fetch the data from the excel based on key
	 * 
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 */
	public String getDataFromExcelBasedOnColumnName(String excelPath, String sheetName, String testcaseName,
			String requiredKey) throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		boolean flag = false;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = sheet.getRow(i).getCell(0).getStringCellValue();
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(testcaseName)) {
				for (int j = 1; j <= sheet.getRow(0).getLastCellNum(); j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
					} catch (Exception e) {
					}
					if (actualKey.equalsIgnoreCase(requiredKey)) {
						try {
							value = df.formatCellValue(sheet.getRow(i).getCell(j), evaluator);
						} catch (Exception e) {
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		workbook.close();
		return value;
	}

	/**
	 * This method is used to fetch the data from the excel based on key
	 * 
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 */
	public HashMap<String, String> getAllColumnDataBasedOnTestCaseName(String excelPath, String sheetName,
			String testcaseName) throws EncryptedDocumentException, IOException {

		HashMap<String, String> map = new HashMap<>();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		DataFormatter df = new DataFormatter();
		if (sheet == null)
			UtilityObjectClass.getExtentNodeTest()
					.fail("Failed to fetch Test Data for <b>" + sheetName
							+ "</b> API <br><b>Reason for failure:- </b><font color='red'>" + sheetName
							+ " Sheet not found in Excel in API TestData Excel</font>");
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(testcaseName)) {
				for (int j = 0; j <= sheet.getRow(0).getLastCellNum() + 1; j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
						try {
							value = df.formatCellValue(sheet.getRow(i).getCell(j), evaluator);
						} catch (Exception e) {
						}
						map.put(actualKey.trim(), value.trim());
					} catch (Exception e) {
					}
				}
				break;
			}
		}
		if (map.size() >= 1)
			UtilityObjectClass.getExtentNodeTest()
					.pass("Successfully Test Data Fetched From the excel for <b>" + sheetName + "</b> API");
		else
			UtilityObjectClass.getExtentNodeTest()
					.fail("<font color='red'> Failed to fetch Test Data for <b>" + sheetName + "</b> API</font>");

		workbook.close();
		return map;
	}

	/**
	 * This method is used to fetch the data from the excel based on key
	 * 
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 */
	public String setDataToExcelBasedOnColumnName(String excelPath, String sheetName, String testcaseName,
			String requiredKey, String data) throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		boolean flag = false;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(testcaseName)) {
				for (int j = 1; j <= sheet.getRow(0).getLastCellNum(); j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
					} catch (Exception e) {
					}
					if (actualKey.equalsIgnoreCase(requiredKey)) {
						try {
							sheet.getRow(i).createCell(j).setCellValue(data);
						} catch (Exception e) {
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		fos.close();
		workbook.close();
		return value;
	}

	/**
	 * This method is used to fetch the data from the excel based on key
	 * 
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 */
	public String setDataToExcel(String excelPath, String sheetName, String testcaseName, String requiredKey,
			String data) throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		boolean flag = false;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(testcaseName)) {
				for (int j = 1; j <= sheet.getRow(0).getLastCellNum(); j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
					} catch (Exception e) {
					}
					if (actualKey.equalsIgnoreCase(requiredKey)) {
						try {
							sheet.getRow(i).createCell(j).setCellValue(data);
						} catch (Exception e) {
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		fos.close();
		workbook.close();
		return value;
	}

	public void setDataToExcel(String excelPath, String sheetName, int rowNum, int celNum, String data)
			throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		sheet.getRow(rowNum).createCell(celNum).setCellValue(data);
		FileOutputStream fos = new FileOutputStream(excelPath);
		workbook.write(fos);
		fos.close();
		workbook.close();
	}

	/**
	 * get commondata from from SuiteRunner excel based on key and column
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param constants
	 * @param columnName
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public String getCommonConfigDataFromExcel(String excelPath, String sheetName, String constants, String columnName)
			throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualTestCaseName = "";
		String actualKey = "";
		boolean flag = false;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			try {
				actualTestCaseName = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			} catch (Exception e) {
			}
			if (actualTestCaseName.equalsIgnoreCase(constants)) {
				for (int j = 0; j <= sheet.getRow(i).getLastCellNum(); j++) {
					try {
						actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
					} catch (Exception e) {
					}
					if (actualKey.equalsIgnoreCase(columnName)) {
						try {
							value = df.formatCellValue(sheet.getRow(i).getCell(j), evaluator);
						} catch (Exception e) {
						}
						flag = true;
						break;
					}
				}
			}
			if (flag) {
				break;
			}
		}
		workbook.close();
		return value;
	}

	/**
	 * get data from Excel base on index
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param rowNum
	 * @param celNum
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public String getDataFromExcel(String excelPath, String sheetName, int rowNum, int celNum)
			throws Throwable, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		String data = "";
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		try {
			data = df.formatCellValue(sheet.getRow(rowNum).getCell(celNum), evaluator);
		} catch (Exception e) {
			data = "";
		}
		workbook.close();
		return data;
	}

	/**
	 * get the entire column data from Excel based on column name
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param celNum
	 * @param rowcount
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public ArrayList<String> getEntireColumnFromExcel(String excelPath, String sheetName, int celNum, int rowcount)
			throws Throwable, IOException {
		DataFormatter df = new DataFormatter();
		ArrayList<String> lst = new ArrayList<>();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		String data = "";
		Sheet sheet = workbook.getSheet(sheetName);

		for (int i = 1; i < rowcount; i++) {
			try {
				lst.add(df.formatCellValue(sheet.getRow(i).getCell(celNum), evaluator));
			} catch (Exception e) {

			}
		}
		workbook.close();
		return lst;
	}

	/**
	 * get the testID's List from 0th column
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	/*
	 * public List<String> getAllRowTestNameLst(String excelPath, String sheetName)
	 * throws Throwable, IOException { openExcel(excelPath); Sheet sheet =
	 * workbook.getSheet(sheetName); String data = ""; List<String> testIdList = new
	 * ArrayList<String>(); int rowLastIndex = sheet.getLastRowNum(); for (int i =
	 * 0; i <= rowLastIndex; i++) { data = sheet.getRow(i).getCell(0).toString(); if
	 * (data.equals("") || data.equals("testID")) { } else { testIdList.add(data); }
	 * } workbook.close(); return testIdList; }
	 */
	/**
	 * get all row data from excel based on column name
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public ArrayList<String> getAllRowDataFromColumn(String excelPath, String sheetName, String expectedColumnName)
			throws Throwable, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String data = "";
		ArrayList<String> testIdList = new ArrayList<String>();
		int rowLastIndex = sheet.getLastRowNum();
		int celCount = sheet.getRow(0).getLastCellNum();
		int coulumnCount = 0;
		for (int j = 0; j < celCount; j++) {
			if (expectedColumnName.equals(df.formatCellValue(sheet.getRow(0).getCell(j), evaluator))) {

				break;
			}
			coulumnCount++;
		}

		for (int i = 1; i <= rowLastIndex; i++) {
			data = df.formatCellValue(sheet.getRow(i).getCell(coulumnCount), evaluator);
			if (data.equals("") || data.equals("TXN_ID_TEST")) {
			} else {
				testIdList.add(data);
			}
		}
		workbook.close();
		return testIdList;
	}

	/**
	 * get the last row index
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public List<String> getRowCountForDispute(String excelPath, String sheetName) throws Throwable, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

		Sheet sheet = workbook.getSheet(sheetName);
		String data = "";
		List<String> testIdList = new ArrayList<String>();
		int rowLastIndex = sheet.getLastRowNum();
		for (int i = 0; i <= rowLastIndex; i++) {
			data = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			if (data.equals("") || data.equals("testID")) {
			} else {
				testIdList.add(data);
			}
		}
		workbook.close();
		return testIdList;
	}

	/**
	 * get the last row index
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public int getRowCount(String excelPath, String sheetName) throws Throwable, IOException {
		openExcel(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		int rowLastIndex = sheet.getLastRowNum();
		workbook.close();
		return rowLastIndex;
	}

	/**
	 * get cell count on expected row
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param rowNum
	 * @return
	 * @throws Throwable
	 * @throws IOException
	 */
	public int getCellCount(String excelPath, String sheetName, int rowNum) throws Throwable, IOException {
		openExcel(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		int rowLastIndex = sheet.getRow(rowNum).getLastCellNum();
		workbook.close();
		return rowLastIndex;
	}

	/**
	 * This method is used to fetch the data from the excel and stored in List<Map>
	 * 
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> getDataFromExcelInList(String sheetName) throws IOException {
		DataFormatter df = new DataFormatter();
		Sheet sheet = workbook.getSheet(sheetName);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int k = 1; k < sheet.getRow(0).getLastCellNum(); k++) {
			Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				map.put(df.formatCellValue(sheet.getRow(i).getCell(0), evaluator),
						df.formatCellValue(sheet.getRow(i).getCell(k), evaluator));
			}
			list.add(map);
		}
		workbook.close();
		return list;
	}

	/**
	 * This method is used close the workbook
	 * 
	 * @throws IOException
	 */
	public void closeExcelWorkBook() throws IOException {
		workbook.close();
	}

	/**
	 * This method is used to fetch the data from the excel and stored in List<Map>
	 * 
	 * @param sheetName
	 * @return
	 * @throws Throwable
	 */
	public List<String> getPageElements(String excelPath, String sheetName, String pageName) throws Throwable {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		List<String> list = new ArrayList<String>();

		int rowCount = getRowCount(excelPath, sheetName);

		for (int i = 1; i <= rowCount; i++) {
			String actPageName = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			if (actPageName.equalsIgnoreCase(pageName)) {

				int lastCelNum = sheet.getRow(i).getLastCellNum();

				for (int j = 1; j < lastCelNum; j++) {
					list.add(df.formatCellValue(sheet.getRow(i).getCell(j), evaluator));
				}

				break;
			}
		}

		if (list.size() == 0) {
			System.out.println(pageName + "==>Page does not Exits");
		}
		workbook.close();
		return list;
	}

	/**
	 * get the target from the Element
	 * 
	 * @param lst
	 * @param requiredTarget
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */

	public String getTarget(List<String> lst, String requiredTarget) throws EncryptedDocumentException, IOException {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		String requiredtarget = "";
		boolean flag = false;
		for (int i = 0; i < lst.size(); i++) {
			String actTarget = lst.get(i).split("#")[0].trim();
			if (actTarget.equalsIgnoreCase(requiredTarget)) {
				requiredtarget = lst.get(i).split("#")[1].trim();
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.err.println(requiredTarget + "Syntax is wrong in Page class");
		}
		if (requiredtarget.equals("")) {
			System.out.println(requiredTarget + " is not availbale");
		}
		workbook.close();
		return requiredtarget.split(">>")[0];

	}

	/**
	 * get the value from the Element
	 * 
	 * @param lst
	 * @param requiredTarget
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public String getTargetValue(List<String> lst, String requiredTarget)
			throws EncryptedDocumentException, IOException {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		String requiredtarget = "";
		boolean flag = false;
		for (int i = 0; i < lst.size(); i++) {
			String actTarget = lst.get(i).split("#")[0].trim();
			if (actTarget.equalsIgnoreCase(requiredTarget)) {
				requiredtarget = lst.get(i).split("#")[1].trim();
				flag = true;
				break;
			}
		}
		if (!flag) {
			System.err.println(requiredTarget + "syntax is wrong in Page class");
		}
		if (requiredtarget.equals("")) {
			System.out.println(requiredTarget + " is not availbale");
		}
		workbook.close();
		return requiredtarget.split(">>")[1];

	}

	/**
	 * This method used to Fetch the complete row data for specified index
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param rowIndex
	 * @return
	 * @throws Throwable
	 */
	public List<String> getRowData(String fileName, String sheetName, int rowIndex) throws Throwable {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		FileInputStream fis = new FileInputStream(fileName);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet = wb.getSheet(sheetName);
		List<String> dataList = new ArrayList<>();
		int cellCount = sheet.getRow(rowIndex).getLastCellNum();
		for (int i = 0; i < cellCount; i++) {
			String headerName = df.formatCellValue(sheet.getRow(0).getCell(i), evaluator);
			dataList.add(headerName);
		}
		wb.close();
		return dataList;
	}

	/**
	 * This method used to Fetch the complete Column data for specified columnName
	 * 
	 * @param fileName
	 * @param sheetName
	 * @param columnName
	 * @return
	 * @throws Throwable
	 */
	public Map<Integer, String> getColumnData(String fileName, String sheetName, String columnName) throws Throwable {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		FileInputStream fis = new FileInputStream(fileName);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet = wb.getSheet(sheetName);
		Map<Integer, String> dataList = new HashMap<>();
		int cellCount = sheet.getRow(0).getLastCellNum();
		for (int i = 0; i < cellCount; i++) {
			String headerName = df.formatCellValue(sheet.getRow(0).getCell(i), evaluator);
			if (headerName.equals(columnName)) {
				for (int j = 0; j <= sheet.getLastRowNum(); j++) {
					String data = df.formatCellValue(sheet.getRow(j).getCell(i), evaluator);
					if (data.equals("") || data.equals(columnName) || data.contains(":"))
						continue;
					dataList.put(j, data);
				}
			}
		}
		wb.close();
		return dataList;
	}

	/**
	 * This method is used to fetch the single data from excel based on row index
	 * and key
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param rowIndex
	 * @param requiredKey
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public String getDataFromExcel(String excelPath, String sheetName, int rowIndex, String requiredKey)
			throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualKey = "";

		for (int j = 0; j < sheet.getRow(rowIndex).getLastCellNum(); j++) {
			try {
				actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
			} catch (Exception e) {
			}
			if (actualKey.equalsIgnoreCase(requiredKey)) {
				try {
					value = df.formatCellValue(sheet.getRow(rowIndex).getCell(j), evaluator);
				} catch (Exception e) {
				}
				break;
			}
		}
		workbook.close();
		return value;
	}

	public String getDataFromExcelForTatExpiry(String excelPath, String sheetName, int rowIndex, String requiredKey)
			throws EncryptedDocumentException, IOException {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		String actualKey = "";

		for (int j = 0; j < sheet.getRow(rowIndex).getLastCellNum(); j++) {
			try {
				actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
			} catch (Exception e) {
			}
			if (actualKey.equalsIgnoreCase(requiredKey)) {
				try {
					value = df.formatCellValue(sheet.getRow(rowIndex).getCell(j), evaluator);
				} catch (Exception e) {
				}
				break;
			}
		}
		workbook.close();
		return value;
	}

	// --------------------------TAT-----------
	public ArrayList<String> getEntireColumnData(String excelPath, String sheetName, String columnName)
			throws EncryptedDocumentException, IOException {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		String actualKey = "";
		ArrayList<String> list = new ArrayList<>();

		for (int j = 0; j < sheet.getRow(0).getLastCellNum(); j++) {
			try {
				actualKey = df.formatCellValue(sheet.getRow(0).getCell(j), evaluator);
			} catch (Exception e) {
			}
			if (actualKey.equalsIgnoreCase(columnName)) {
				for (int i = 1; i <= sheet.getLastRowNum(); i++)
					try {
						list.add(df.formatCellValue(sheet.getRow(i).getCell(j), evaluator));
					} catch (Exception e) {
					}
				break;
			}
		}
		workbook.close();
		return list;
	}

	/**
	 * This Method is used to Fetch data from excel based on keys
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param requiredKey
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public String getDataFromExcel(String excelPath, String sheetName, String requiredKey)
			throws EncryptedDocumentException, IOException {
		DataFormatter df = new DataFormatter();
		openExcel(excelPath);
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Sheet sheet = workbook.getSheet(sheetName);
		String value = "";
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			String actualKey = df.formatCellValue(sheet.getRow(i).getCell(0), evaluator);
			if (actualKey.equalsIgnoreCase(requiredKey)) {
				value = df.formatCellValue(sheet.getRow(i).getCell(1), evaluator);
			}
		}
		workbook.close();
		return value;
	}
}
