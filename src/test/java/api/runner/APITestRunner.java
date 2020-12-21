package api.runner;

import api.constants.RequestType;
import api.helpers.RestAssuredHelper;
import api.tests.BaseTests;
import io.cucumber.java.Before;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.json.simple.parser.ParseException;

import java.io.IOException;

@CucumberOptions(
        features={"src/test/resources/features"},
        glue={"api.tests"},
        plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        monochrome = true
)
public class APITestRunner extends AbstractTestNGCucumberTests {


}

