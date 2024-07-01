package org.tyss.knolskape.genericutility;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class JiraUtility {
	FileUtility fileUtility = new FileUtility();
	String BASE_URL = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, "BASE_URL");
	String PROJECT_KEY = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, "PROJECT_KEY");
	String AUTH_TOKEN = fileUtility.getDataFromPropertyFile(IConstants.PROPERTY_FILE_PATH, "AUTH_TOKEN");

	public String createTestCycle(String cycleName) {
		RestAssured.baseURI = BASE_URL;
		JSONObject requestParams = new JSONObject();
		requestParams.put("projectKey", PROJECT_KEY);
		requestParams.put("name", cycleName);
		requestParams.put("description", "Test Cycle for Knolskape script execution.");

		Response response = RestAssured
				.given()
				.header("Content-Type", "application/json")
				.header("Authorization", AUTH_TOKEN)
				.body(requestParams.toString())
				.post("testcycles")
				.then()
				.extract()
				.response();

		if (response.getStatusCode() == 201) {
			System.out.println("Test cycle created with name: "+cycleName);
			return response.jsonPath().getString("key");
		} else {
			System.err.println("Failed to create test cycle. Status code: " + response.getStatusCode());
			System.err.println("Response body: " + response.getBody().asString());
			return null;
		}
	}

	public void addTestCaseToCycleAndUpdateResults(String cycleId, String testCaseId, String status) {
		RestAssured.baseURI = BASE_URL;

		JSONObject requestParams = new JSONObject();
		requestParams.put("projectKey", PROJECT_KEY);
		requestParams.put("testCycleKey", cycleId);
		requestParams.put("testCaseKey", testCaseId);
		requestParams.put("statusName", status);

		Response response = RestAssured
				.given()
				.header("Content-Type", "application/json")
				.header("Authorization", AUTH_TOKEN)
				.body(requestParams.toString())
				.post("testexecutions")
				.then()
				.extract()
				.response();

		if (response.getStatusCode() == 201) {
			System.out.println("Test Case: "+testCaseId+" added to the Cycle with status: "+status);
		} else {
			System.err.println("Failed to add test case to cycle. Status code: " + response.getStatusCode());
			System.err.println("Response body: " + response.getBody().asString());
		}
	}
}
