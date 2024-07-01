package org.tyss.knolskape.testcases;

import java.io.IOException;

import org.tyss.knolskape.workflowutility.CommonWorkflowsUtility;

public class Test  {
	
	public static void main(String[] args) throws IOException {
		CommonWorkflowsUtility cwu = new CommonWorkflowsUtility();
		cwu.signInToApplication(null, null, null);
	}
}
