package org.tyss.knolskape.genericutility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * This class contains the methods to fetch the data from Property File
 *
 */

public class FileUtility {
	/**
	 * This method is used to fetch the data from Property File
	 * @param Path
	 * @param key
	 * @return
	 */
	public String getDataFromPropertyFile(String filePath, String key) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = p.getProperty(key).trim();
		return value;
	}

}
