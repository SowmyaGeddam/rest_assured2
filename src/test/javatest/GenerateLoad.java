package tests;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ReadExcelData;
import utils.WriteToExcel;

public class GenerateLoad extends BaseTest {

	ReadExcelData readExcelData = new ReadExcelData();

	public static List<Object[]> results = new ArrayList<Object[]>();

	@SuppressWarnings("static-access")
	@DataProvider(name = "DataFromExcel")
	public Object[][] dataForTest() {
		return readExcelData.excelData;
	}

	// method for validating Headers
	public String getHeaders(Response response, String[] headers) {
		String joinHeaders = "";
		for (String header : headers) {
			joinHeaders = response.getHeader(header);
			joinHeaders = joinHeaders + ",";
		}
		return joinHeaders;
	}

	// method for validating jsonPath
	public void validateJsonPaths(Response response, String[] jsonPaths) {
		for (String jsonpath : jsonPaths) {
			String[] jsonValues = jsonpath.split("=");
			String expected = jsonValues[0];
			String found = jsonValues[1];
			Assert.assertEquals(response.jsonPath().getString(expected), found);
		}
	}

	@Test(dataProvider = "DataFromExcel")
	public void validateRequest(String requestType, String requestURI, String requestHeaders, String requestBody,
			String requestParameters) {

		Response response;
		RequestSpecification responseSpecification = RestAssured.given().contentType(ContentType.JSON).when();
		String[] splitParameters;
		String[] splitHeaders;

		int statuscode;
		int productId;
		String responseBody;
		String responseHeader;

		results.add(new Object[] { "Response Status Code", "Response Body", "Response Header" });

		switch (requestType) {
		case "GET":
			splitHeaders = requestHeaders.split(",");
			splitParameters = requestParameters.split(",");
			response = responseSpecification.queryParam(splitParameters[0], Integer.parseInt(splitParameters[1]))
					.get(requestURI).then().extract().response();

			statuscode = response.getStatusCode();
			responseBody = response.asPrettyString();
			responseHeader = getHeaders(response, splitHeaders);
			results.add(new Object[] { statuscode, responseBody, responseHeader });

			break;

		case "POST":
			splitHeaders = requestHeaders.split(",");
			response = responseSpecification.body(requestBody).post(requestURI).then().extract().response();

			// validate headers
			// calling header validation method
			responseHeader = getHeaders(response, splitHeaders);
			responseBody = response.asPrettyString();
			statuscode = response.getStatusCode();
			results.add(new Object[] { statuscode, responseBody, responseHeader });
			break;

		case "PUT":
			splitHeaders = requestHeaders.split(",");

			// As we need to post something firest in order to put or partially update it
			// later with the given parameter
			response = responseSpecification.body(requestBody).post(requestURI).then().extract().response();

			productId = response.path(requestParameters);
			requestURI = requestURI + "/";
			int statusCodeOnPut = responseSpecification.body(requestBody).put(requestURI + productId).then().extract()
					.response().statusCode();

			statuscode = statusCodeOnPut;
			responseBody = response.asPrettyString();
			responseHeader = getHeaders(response, splitHeaders);
			results.add(new Object[] { statuscode, responseBody, responseHeader });
			break;

		case "PATCH":
			splitHeaders = requestHeaders.split(",");

			// As we need to post something firest in order to put or partially update it
			// later with the given parameter
			response = responseSpecification.body(requestBody).post(requestURI).then().extract().response();

			productId = response.path(requestParameters);
			requestURI = requestURI + "/";
			int statusCodeOnPatch = responseSpecification.body(requestBody).patch(requestURI + productId).then()
					.extract().response().statusCode();

			statuscode = statusCodeOnPatch;
			responseBody = response.asPrettyString();
			responseHeader = getHeaders(response, splitHeaders);
			results.add(new Object[] { statuscode, responseBody, responseHeader });
			break;

		case "DELETE":
			splitHeaders = requestHeaders.split(",");
			response = responseSpecification.body(requestBody).post(requestURI).then().extract().response();
			productId = response.path(requestParameters);
			requestURI = requestURI + "/";
			int statusCodeOnDelete = responseSpecification.delete(requestURI + productId).then().extract().response()
					.statusCode();

			statuscode = statusCodeOnDelete;
			responseBody = response.asPrettyString();
			responseHeader = getHeaders(response, splitHeaders);
			results.add(new Object[] { statuscode, responseBody, responseHeader });

			break;

		default:
			break;
		}

	}

	@AfterTest
	public void writeToExcel() {
		WriteToExcel writeToExcel = new WriteToExcel(results);
	}

}