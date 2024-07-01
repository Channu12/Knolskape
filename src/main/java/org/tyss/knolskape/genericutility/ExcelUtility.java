package org.tyss.knolskape.genericutility;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility {

	public String getDataFromExcelCellBasedOnUniqueDataAndHeader(String excelPath, String sheetName,
			String uniqueData, String header) throws EncryptedDocumentException, IOException {
		String value = "";
		FileInputStream fis = new FileInputStream(excelPath);
		Workbook workbook = WorkbookFactory.create(fis);
		Sheet sheet = workbook.getSheet(sheetName);
		int rowCount = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < rowCount; i++) {
			String actualUniqueData = sheet.getRow(i).getCell(0).getStringCellValue();
			if (actualUniqueData.equalsIgnoreCase(uniqueData)) {
				int columnCount = sheet.getRow(i).getPhysicalNumberOfCells();
				for (int j = 1; j < columnCount; j++) {
					String actualHeader = sheet.getRow(0).getCell(j).getStringCellValue();
					if (actualHeader.equalsIgnoreCase(header)) {
						value = sheet.getRow(i).getCell(j).getStringCellValue();
						break;
					}
				}
				if (!value.isEmpty()) {
					break;
				}
			}
		}
		workbook.close();
		fis.close();
		return value;
	}
}
