package CucumberWeb.RunnerClasses;


import CucumberWeb.Utilities.DataHelper;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by geetha on 07-07-2019.
 */
@CucumberOptions(
        features={"src/test/resources/features"},
        glue={"CucumberWeb.StepDefinition"},
        plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"},
        monochrome = true
)

public class CukesRunnerTest extends AbstractTestNGCucumberTests {
    private File directory = new File((getFolder()));

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }

    //Building up the destination path for the screenshot to save
    //Also make sure to create a folder 'Run_<UniqueNumber>' with in the cucumber-report folder
    private static String FOLDERNAME = "Run_" + System.currentTimeMillis();
    public static final String ALLURE_FOLDER = System.getProperty("user.dir") + "/target/allure-results";
    public static final String TestDataPath = System.getProperty("user.dir")+"/src/test/resources/TestData/TestData.xlsm";
    public static final Map<String, List<String>> hm = DataHelper.data();
    public static final String ExecSheetName="Test_Execution";
    public static final String DataSheetName="Test_Data";


    public static String getFolder(){
        return System.getProperty("user.dir") + "/target/output/" + FOLDERNAME;
    }

    @BeforeSuite
    public void setup() throws Exception {

        if(!directory.exists()){

            directory.mkdir();

        }
    }
}

