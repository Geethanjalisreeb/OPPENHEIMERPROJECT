package api.helpers;

import api.constants.RequestType;
import api.tests.BaseTests;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class RestAssuredHelper {

	private JsonProcessing jsonProcessing = new JsonProcessing();
	private RequestSpecification request = RestAssured.given();
	private Response response = null;

	private void GetRequestHeader() {
		BaseTests baseTest = new BaseTests();
		request.headers(baseTest.getHeader());
		Allure.addAttachment("Request Header:","text/plain",baseTest.getHeader().toString());
	}

	private void SendRequest(RequestType requestType, String url){
		switch(requestType)
		{
			case Delete:
				response = request.delete(url);
				break;
			case Get:
				response = request.get(url);
				break;
			case Patch:
				response = request.patch(url);
				break;
			case Post:
				response = request.post(url);
				break;
			case Put:
				response = request.put(url);
				break;
			default:
				throw new UnsupportedOperationException("Request type is not supported.");
		}
	}

	@SuppressWarnings("unchecked")
	@Step ("Send {0} Request with request body as given in request body in parameter")
	public Response getResponse(RequestType requestType, String url, JSONObject jsonRequestBody) throws IOException, ParseException {

		GetRequestHeader();

		request.body(jsonRequestBody.toString());
		Allure.addAttachment("Request Body:","text/json" ,jsonProcessing.formatJSONString(jsonRequestBody), "json");

		SendRequest(requestType,url);

		return response;
	}

	@Step ("Send {0} Request with request body as given in request body in parameter")
	public Response getResponse(RequestType requestType, String url, JSONArray jsonRequestBody) throws IOException, ParseException {

		GetRequestHeader();

		request.body(jsonRequestBody.toString());
		Allure.addAttachment("Request Body:","text/json" ,jsonProcessing.formatJSONString(jsonRequestBody), "json");

		SendRequest(requestType,url);

		return response;
	}

	@SuppressWarnings("unchecked")
	@Step ("Send {0} Request with no request body ")
	public Response getResponse(RequestType requestType, String url) throws IOException, ParseException {

		GetRequestHeader();

		SendRequest(requestType,url);

		return response;
	}

	@SuppressWarnings("unchecked")
	@Step ("Send {0} Request with request body as in file '{2}.json'")
	public Response getResponse(RequestType requestType, String url, String JsonFileName) throws IOException, ParseException {
		GetRequestHeader();

			String FilePath = BaseTests.RequestFilePath + JsonFileName + ".json";
			File RequestPath= new File(FilePath);
			FileReader reader = new FileReader(RequestPath);
			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			request.body(jsonObject.toString());
			InputStream requestBody = Files.newInputStream(Paths.get(FilePath));
			Allure.addAttachment("Request Body:","text/json" ,requestBody,"json");

		SendRequest(requestType,url);
		return response;
	}
}