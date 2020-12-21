package api.helpers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.skyscreamer.jsonassert.FieldComparisonFailure;
import org.skyscreamer.jsonassert.JSONCompareResult;

import static org.skyscreamer.jsonassert.JSONCompare.compareJSON;
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;

public class JsonProcessing {

	private ObjectMapper objectMapper=new ObjectMapper();

	public JsonProcessing() {

	}

	public String ConvertModelToJSON(Object model) 
	{		
		String postModelAsString = null;
		try 
		{
			postModelAsString = objectMapper.writeValueAsString(model);
			return postModelAsString;
		} 
		catch (JsonProcessingException e) 
		{
			e.printStackTrace();
		}
		
		return postModelAsString;		
	}
	
	public Map<?, ?> ConvertModelToMap(Object model) 
	{
		Map<?, ?> mappedObject = objectMapper.convertValue(model, Map.class);
		return mappedObject;
	}

	public Map<?, ?> convertJSONStringToMap(String jsonstring) throws IOException {
		Map<?, ?> mappedObject = objectMapper.readValue(jsonstring, Map.class);
		return mappedObject;
	}

	public List<HashMap>convertJSONStringToList(String jsonstring) throws IOException {
		List<HashMap> mappedObject = objectMapper.readValue(jsonstring, List.class);
		return mappedObject;
	}

	public String convertMapToJSONString(Map<?, ?> MappedObject) throws IOException {
		String jsonstring = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(MappedObject);
		return jsonstring;
	}

	public String formatJSONString(JSONObject jsonObject) throws IOException {
		String jsonstring = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		return jsonstring;
	}

	public String formatJSONString(JSONArray jsonObject) throws IOException {
		String jsonstring = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
		return jsonstring;
	}

	public JSONObject convertMapToJson(Map<?, ?> MappedObject) throws IOException, ParseException {
		String jsonResp = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(MappedObject);
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(jsonResp);
		JSONObject jsonObject = (JSONObject) obj;
		return jsonObject;
	}
	public JSONArray convertMapListToJson(List<HashMap> MappedObject) throws IOException, ParseException {
		String jsonResp = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(MappedObject);
		JSONParser jsonParser = new JSONParser();
		Object obj = jsonParser.parse(jsonResp);
		JSONArray jsonObject = (JSONArray) obj;
		return jsonObject;
	}
	public JSONObject getJsonFromFile(String FilePath) throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		File ResponsePath = new File(FilePath);
		FileReader reader = new FileReader(ResponsePath);
		Object obj = jsonParser.parse(reader);
		JSONObject jsonObject = (JSONObject) obj;
		return jsonObject;
	}
	public Map<String, List<Object>> jsonComparisonMap(JSONObject Expected, JSONObject Actual, String Compare) throws IOException, ParseException {
		Map<String, List<Object>> jsonDiffResults = new HashMap<>();
		JSONCompareResult jsonCompareResult = compareJSON(Expected.toJSONString(), Actual.toJSONString(), STRICT);
		switch (Compare.toUpperCase().trim()) {
			case "FIELDFAILURE":
				if (!(jsonCompareResult.getFieldFailures().isEmpty())) {
					for (FieldComparisonFailure failure : jsonCompareResult.getFieldFailures()) {

						String Field = failure.getField();
						List<Object> currentList = new ArrayList<>();
						currentList.add(failure.getActual());
						currentList.add(failure.getExpected());
						jsonDiffResults.put(Field,currentList);
					}
					break;
				}
			case "FIELDMISSING":
				if (!(jsonCompareResult.getFieldMissing().isEmpty())) {
					for (FieldComparisonFailure failure : jsonCompareResult.getFieldMissing()) {

						String Field = failure.getField() + "." + failure.getExpected().toString();
						List<Object> currentList = new ArrayList<>();
						currentList.add(failure.getActual());
						currentList.add(failure.getExpected());
						jsonDiffResults.put(Field,currentList);

					}
				}
				break;
			case "FIELDUNEXPECTED":
				if (!(jsonCompareResult.getFieldUnexpected().isEmpty())) {
					for (FieldComparisonFailure failure : jsonCompareResult.getFieldUnexpected()) {

						String Field = failure.getField() + "." + failure.getActual().toString();
						List<Object> currentList = new ArrayList<>();
						currentList.add(failure.getActual());
						currentList.add(failure.getExpected());
						jsonDiffResults.put(Field,currentList);

					}
				}
				break;
			default:
				throw new UnsupportedOperationException("Comparison type is not supported.");
		}

		return jsonDiffResults;
	}
	public ObjectMapper getobjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
}