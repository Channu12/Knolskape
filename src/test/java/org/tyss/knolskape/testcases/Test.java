package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.tyss.knolskape.genericutility.JavaUtility;

public class Test {

	public static void main(String[] args) throws IOException {
		JavaUtility java = new JavaUtility();
		java.compareTwoImages("C:\\Users\\User\\Desktop\\Comparison\\a.png", "C:\\Users\\User\\Desktop\\Comparison\\b.png", "C:\\Users\\User\\Desktop\\Comparison");
	}
}
