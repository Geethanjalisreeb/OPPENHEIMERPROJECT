package api.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


import api.helpers.JsonProcessing;
import api.helpers.RestAssuredHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.JSONTokener;



public class BaseTests
{
	public static final String fileSeparator = System.getProperty("file.separator");
	public static final String resourceFilePath = System.getProperty("user.dir")+fileSeparator+"src"+fileSeparator+"test"+fileSeparator+"resources"+fileSeparator;
	public static final String ResponseFilePath = resourceFilePath + "APIData/ResponseData" + fileSeparator;
	public static final String RequestFilePath = resourceFilePath + "APIData/RequestData" + fileSeparator;
	public static final String JSONSchemaPath =  resourceFilePath + "APIData/JSONSchema" + fileSeparator;

	private JsonProcessing data = new JsonProcessing();
	private RestAssuredHelper restAssuredHelper = new RestAssuredHelper();


	public BaseTests()
	{
		setBaseURI();
	}
	
	public void setBaseURI()
	{
		// read config.properties file
		try (FileInputStream input = new FileInputStream(resourceFilePath + "APITestConfig.properties")) {

			// load the properties file
			Properties prop = new Properties();
			prop.load(input);

			// get the BaseURI value
			RestAssured.baseURI 	= prop.getProperty("baseURI");

		} catch (IOException ex) {
            System.out.println("Cannot open config.properties file" + ex);
		}
	}

	public Map<String,Object> getHeader()
	{
		Map<String,Object> HMap = new HashMap<String,Object>();
		String[] HeaderNames ;
		String[] HeaderValues;
		// read config.properties file
		try (FileInputStream input = new FileInputStream(resourceFilePath + "APITestConfig.properties")) {

			// load the properties file
			Properties prop = new Properties();
			prop.load(input);

			// get the header values
			HeaderNames 			= prop.getProperty("HeaderName").split(",");
			HeaderValues 			= prop.getProperty("HeaderValue").split(",");

			for (int i=0;i<HeaderNames.length;i++){
				HMap.put(HeaderNames[i],HeaderValues[i]);
			}

		} catch (IOException ex) {
            System.out.println("Cannot open config.properties file" + ex);
		}
		return HMap;
	}

	public JSONObject formRequestResponse(String Type, List<List<String>> DataMapList, String FileName) throws IOException, ParseException {

		String FilePath;
		switch(Type.toUpperCase().trim())
		{
			case "REQUEST":
				FilePath = RequestFilePath + FileName + ".json";
				break;
			case "RESPONSE":
				FilePath = ResponseFilePath + FileName + ".json";
				break;
            case "SCHEMA":
                FilePath = JSONSchemaPath + FileName + ".json";
                break;
			default:
				throw new UnsupportedOperationException(Type+": Type given is not supported.");
		}


		JSONObject RequestBody;
		//JSONObject BaseRequestBody = data.getJsonFromFile(FilePath);

		//String RequestBodystr = BaseRequestBody.toString();
		String RequestBodystr = readFile(FilePath);
		List<String> HeaderList = DataMapList.get(0);
		List<String> ValueList = DataMapList.get(1);

		for(int i=0; i<HeaderList.size(); i++) {
			RequestBodystr=RequestBodystr.replace(HeaderList.get(i),ValueList.get(i));
		}
		Map<?,?> reqbodymap = data.convertJSONStringToMap(RequestBodystr);
		RequestBody=data.convertMapToJson(reqbodymap);
		//System.out.println(RequestBody);
		return RequestBody;
	}

	public JSONArray formRequestResponseArr(String Type, List<List<String>> DataMapList, String FileName) throws IOException, ParseException {

		String FilePath;
		switch(Type.toUpperCase().trim())
		{
			case "REQUEST":
				FilePath = RequestFilePath + FileName + ".json";
				break;
			case "RESPONSE":
				FilePath = ResponseFilePath + FileName + ".json";
				break;
			case "SCHEMA":
				FilePath = JSONSchemaPath + FileName + ".json";
				break;
			default:
				throw new UnsupportedOperationException(Type+": Type given is not supported.");
		}


		JSONArray RequestBody;
		//JSONObject BaseRequestBody = data.getJsonFromFile(FilePath);

		//String RequestBodystr = BaseRequestBody.toString();
		String RequestBodystr = readFile(FilePath);
		List<String> HeaderList = DataMapList.get(0);
		List<String> ValueList = DataMapList.get(1);

		for(int i=0; i<HeaderList.size(); i++) {
			RequestBodystr=RequestBodystr.replace(HeaderList.get(i),ValueList.get(i));
		}
		List<HashMap> reqbodymap = data.convertJSONStringToList(RequestBodystr);
		RequestBody=data.convertMapListToJson(reqbodymap);
		//System.out.println(RequestBody);
		return RequestBody;
	}

	public JSONObject getActualResponse(Response actualresponse) throws IOException, ParseException {
		Map<?, ?> actualResponseBody = actualresponse.jsonPath().get();
		JSONObject actualJsonObject = data.convertMapToJson(actualResponseBody);
		//Save Actual responses as JSON attachments in allure report
		Allure.addAttachment("Actual Response", "text/json", data.convertMapToJSONString(actualResponseBody), "json");
		return actualJsonObject;
	}

	public JSONObject getExpectedResponsefromFile(String FilePath) throws IOException, ParseException {
		JSONObject expectedJsonObject = data.getJsonFromFile(FilePath);
		//Save Expected and Actual responses as JSON attachments in allure report
		InputStream expectedResponse = Files.newInputStream(Paths.get(FilePath));
		Allure.addAttachment("Expected Response:", "text/json", expectedResponse,"json" );
		return expectedJsonObject;
	}

	public void partialCompare(JSONObject expectedjsonObject, JSONObject actualjsonObject, String IgnoreListStr) throws IOException, ParseException {
		Map<?, ?> FieldFailureMap = data.jsonComparisonMap(expectedjsonObject,actualjsonObject,"FIELDFAILURE");
		Map<?, ?> FieldMissingMap = data.jsonComparisonMap(expectedjsonObject,actualjsonObject,"FIELDMISSING");
		Map<?, ?> FieldUnexpectedMap = data.jsonComparisonMap(expectedjsonObject,actualjsonObject,"FIELDUNEXPECTED");
		Boolean IsFieldFailure = Boolean.FALSE;
		Boolean IsFieldMissing = Boolean.FALSE;
		Boolean IsFieldUnexpected= Boolean.FALSE;
		String MissingFields= "No Mismatch Found";
		String UnexpectedFields="No Mismatch Found";
		String FieldFailure="No Mismatch Found";



		if (!FieldMissingMap.isEmpty()){
			MissingFields = Arrays.toString(FieldMissingMap.keySet().toArray());
			IsFieldMissing = Boolean.TRUE;
		}
		if (!FieldUnexpectedMap.isEmpty()){
			UnexpectedFields = Arrays.toString(FieldUnexpectedMap.keySet().toArray());
			IsFieldUnexpected = Boolean.TRUE;
		}
		String[] IgnoreList = IgnoreListStr.split(",");
		List<String> al = new ArrayList<String>();
		al = Arrays.asList(IgnoreList);
		if (!FieldFailureMap.isEmpty()) {
			List<String> FieldFailureList = new ArrayList<String>();
			for (Object Field : FieldFailureMap.keySet().toArray()) {
				if (!(al.contains(Field.toString()))) {
					FieldFailureList.add(Field.toString());
				}
			}
			if (!(FieldFailureList.isEmpty())) {
				FieldFailure = Arrays.toString(FieldFailureList.toArray());
				IsFieldFailure = Boolean.TRUE;
			}
		}

		if (IsFieldFailure || IsFieldMissing || IsFieldUnexpected){
			fail("Fields Missing: " + MissingFields + System.lineSeparator()
					+"Fields Unexpected: " + UnexpectedFields + System.lineSeparator()
					+ "Actual and expected values not matched for fields: " + FieldFailure);

		}
	}

	public String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}

	public String getNodeValue(String tagpath, Response actualresponse) {
		String NodeValue ="";
		if (!(actualresponse.path(tagpath)==null)){
			NodeValue=actualresponse.path(tagpath);
		}
		else{
			fail(tagpath + ": Node does not exist in the actual response");
		}
		return NodeValue;
	}

	@Step("Verify actual status code matches with expected value '{1}'")
	public void assertStatusCode(Response response, int expectedStatusCode)
	{
		int actualStatusCode = response.getStatusCode();
        Allure.addAttachment("Actual Status Code: ", String.valueOf(actualStatusCode));
		assertEquals(expectedStatusCode, actualStatusCode);
	}

    @Step("Verify actual response matches with expected response in file '{0}.json'")
	public void assertContent(String FileName, Response actualresponse) throws IOException, ParseException {

		JSONObject actualJsonObject = getActualResponse(actualresponse);
		JSONObject expectedjsonObject =getExpectedResponsefromFile(ResponseFilePath + FileName + ".json");

		assertEquals(expectedjsonObject, actualJsonObject);
	}

	public void assertContent(JSONObject expectedresponse, Response actualresponse) throws IOException, ParseException {
		JSONObject actualJsonObject = getActualResponse(actualresponse);

		//Save Expected and Actual responses as JSON attachments in allure report
		Allure.addAttachment("Expected Response:", "text/json", data.formatJSONString(expectedresponse),"json" );

		assertEquals(expectedresponse, actualJsonObject);
	}

    @Step("Verify node, '{0}' is present in the actual response")
	public void assertNode(String tagpath, Response actualresponse) throws IOException, ParseException {

            if (!(actualresponse.path(tagpath)==null)){
                String val=actualresponse.path(tagpath);
				Allure.addAttachment("Value of node, " + tagpath + ": ", val);
            }
            else{
                fail(tagpath + ": Node does not exist in the actual response");
            }

	}

    @Step("Verify actual value of node, '{0}' in the response matches expected value '{1}'")
	public void assertNodeValue(String tagpath, String Value, Response actualresponse) throws IOException {
		if (!(actualresponse.path(tagpath)==null)){
			String val=actualresponse.path(tagpath);
			Allure.addAttachment("Actual Value of " + tagpath + ": ", val);
			Allure.addAttachment("Expected Value of " + tagpath + ": ", Value);
			assertEquals(val, Value);
		}
		else {
			fail(tagpath + ": Node does not exist in the actual response");
			//System.out.println(tagpath + ": Node does not exist");
		}

	}

	@Step("Verify actual response matches with expected response in file, '{0}.json' except fields \"{2}\"")
	public void assertContentPartial(String FileName, Response ActualResponse, String IgnoreListStr) throws IOException, ParseException {

	JSONObject actualJsonObject = getActualResponse(ActualResponse);

	JSONObject expectedjsonObject =getExpectedResponsefromFile(ResponseFilePath + FileName + ".json");

	partialCompare(expectedjsonObject,actualJsonObject,IgnoreListStr);


}

	@Step("Verify actual response matches with expected response in file, '{0}.json' except fields \"{2}\"")
	public void assertContentPartial(JSONObject expectedJsonObject, Response ActualResponse, String IgnoreListStr) throws IOException, ParseException {

		JSONObject actualJsonObject = getActualResponse(ActualResponse);

		//Save Expected and Actual responses as JSON attachments in allure report
		Allure.addAttachment("Expected Response:","text/json" ,data.formatJSONString(expectedJsonObject), "json");

		partialCompare(expectedJsonObject,actualJsonObject,IgnoreListStr);

	}

	@Step("Verify JSON schema for actual response matches with schema in file, '{1}.json'")
	public void verifyJSONSchemaForResponse(Response actualresponse, String schemaFileName) throws IOException, ParseException {
		Map<?, ?> actualResponseBody = actualresponse.jsonPath().get();
		JSONObject actualJsonObject = data.convertMapToJson(actualResponseBody);
		String json = actualJsonObject.toString();
		String FilePath = JSONSchemaPath + schemaFileName + ".json";
		File schemaFile = new File(FilePath);
		InputStream expectedSchema = Files.newInputStream(Paths.get(FilePath));


        //Save Expected and Actual responses as JSON attachments in allure report
		Allure.addAttachment("Expected JSON Schema:","text/json",expectedSchema,"json");
        Allure.addAttachment("Actual Response:","text/json",data.convertMapToJSONString(actualResponseBody),"json");

		assertThat(json, matchesJsonSchema(schemaFile));

	}


	public JsonProcessing getData() {
		return data;
	}

	public void setData(JsonProcessing data) {
		this.data = data;
	}

	 public RestAssuredHelper getRestAssuredHelper() {
		return restAssuredHelper;
	}

	 public void setRestAssuredHelper(RestAssuredHelper restAssuredHelper) {
		this.restAssuredHelper = restAssuredHelper;
	}
}