package api.tests;

import api.constants.RequestType;
import api.helpers.RestAssuredHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;


public class APICucumberSteps extends BaseTests {

    private Response response=null;
    private RestAssuredHelper restAssuredHelper = new RestAssuredHelper();
    private static boolean flag = false;

    @Given("^Database is cleaned up before test execution$")
    public void i_receive_the_response() throws IOException, ParseException {
        if(flag==false) {
            flag=true;
            restAssuredHelper.getResponse(RequestType.Post, "/calculator/rakeDatabase");
        }
    }

    @When("^I send post request with path \"([^\"]*)\" and request as in \"([^\"]*)\"$")
    public void i_send_request_with_path_and_request_as_in(String arg1, String arg2) throws IOException, ParseException {
        response = restAssuredHelper.getResponse(RequestType.Post, arg1, arg2);
    }


    @Then("^I should validate status code is \"([^\"]*)\"$")
    public void i_should_validate_status_code_is(String arg1) {
        assertStatusCode(response,Integer.parseInt(arg1));

    }

    @Then("^I should verify the value matches response in \"([^\"]*)\" except fields \"([^\"]*)\"$")
    public void i_should_verify_the_value_matches_response_in_except_fields(String arg1, String arg2) throws IOException, ParseException {
        assertContentPartial(arg1,response, arg2);
    }

    @Then("^I should verify the value matches response in \"([^\"]*)\"$")
    public void i_should_verify_the_value_matches_response(String arg1) throws IOException, ParseException {
        assertContent(arg1,response);
    }

    @Then("^I should verify JSONSchema matches schema as in \"([^\"]*)\"$")
    public void i_should_verify_JSONSchema_matches_schema_as_in(String arg1) throws IOException, ParseException {
            verifyJSONSchemaForResponse(response, arg1);
    }

    @When("^I send post request with path \"([^\"]*)\" and request as in \"([^\"]*)\" with following values$")
    public void iSendPostRequestWithPathAndRequestAsInWithFollowingValues(String arg1, String arg2, DataTable dataTable) throws IOException, ParseException {
        List <List <String>> list = dataTable.asLists(String.class);
        if(arg1.equals("/calculator/insertMultiple")){
            JSONArray RequestBody = formRequestResponseArr("REQUEST",list,arg2);
            response = restAssuredHelper.getResponse(RequestType.Post, arg1, RequestBody);
        }
        else{
            JSONObject RequestBody = formRequestResponse("REQUEST",list,arg2);
            response = restAssuredHelper.getResponse(RequestType.Post, arg1, RequestBody);
        }
    }

    @And("^I should verify the value matches response in \"([^\"]*)\" except fields \"([^\"]*)\" with following values$")
    public void iShouldVerifyTheValueMatchesResponseInExceptFieldsWithFollowingValues(String arg1, String arg2, DataTable dataTable) throws IOException, ParseException {
        List <List <String>> list = dataTable.asLists(String.class);
        JSONObject expectedresponse = formRequestResponse("RESPONSE",list,arg1);
        assertContentPartial(expectedresponse,response,arg2);
    }


    @And("I send post request with path {string}")
    public void iSendPostRequestWithPath(String arg0) throws IOException, ParseException {
        response = restAssuredHelper.getResponse(RequestType.Post, arg0);
    }

    @Then("I should verify the value matches response in {string} with following values")
    public void iShouldVerifyTheValueMatchesResponseInWithFollowingValues(String arg0,DataTable dataTable) throws IOException, ParseException {

        List <List <String>> list = dataTable.asLists(String.class);
        if(arg0.equals("GetTaxRelief")){
            JSONArray RequestBody = formRequestResponseArr("RESPONSE",list,arg0);
            response = restAssuredHelper.getResponse(RequestType.Post, arg0, RequestBody);
        }
        else {
            JSONObject expectedresponse = formRequestResponse("RESPONSE", list, arg0);
            assertContent(expectedresponse, response);
        }
    }

    @When("I send get request with path {string}")
    public void iSendGetRequestWithPath(String arg0) throws IOException, ParseException {
        response = restAssuredHelper.getResponse(RequestType.Get, arg0);
    }
}
