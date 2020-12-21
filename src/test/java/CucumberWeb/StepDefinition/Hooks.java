package CucumberWeb.StepDefinition;

import CucumberWeb.RunnerClasses.CukesRunnerTest;
import CucumberWeb.SeleniumConfig.DriverFactory;
import CucumberWeb.Utilities.DataHelper;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


/**
 * Created by geetha on 07-07-2019.
 */
public class Hooks {
    private File screenshotFile;
    private Scenario scenario;
    private static Logger log = Logger.getLogger(Hooks.class.getName());
    private DataHelper dataHelper = new DataHelper();


    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public void setScreenshotFile(File screenshotFile) {
        this.screenshotFile = screenshotFile;
    }

    public File getScreenshotFile() {
        return screenshotFile;
    }

    public static String getUrl() throws Exception {
        String url = null;
        try {
            // get the url of Application Under Test
            url = getWebProperty("AUT");
        } catch (Exception e) {
            log.error(e.toString());
        }
        return url;
    }

    public static String getBrowser() throws Exception {
        // read config.properties file
        String browser = null;
        try {
            // get the Browser value
            browser = getWebProperty("Browser");
        } catch (Exception e) {
            log.error(e.toString());
        }
        if (browser != null){
            return browser.toLowerCase();
        }else {
            return browser;
        }
    }

    public static String getZaleniumEnabled() throws Exception {
        String ZaleniumEnabled = null;
        try {
            // get the Browser value
            ZaleniumEnabled = getWebProperty("EnableZalenium");
        } catch (Exception e) {
            log.error(e.toString());
        }
        return ZaleniumEnabled;
    }

    public static String getZaleniumURL() throws Exception {
        String zaleniumurl = null;
        try {
            // get the Browser value
            zaleniumurl = getWebProperty("ZaleniumURL");
        } catch (Exception e) {
            log.error(e.toString());
        }
        return zaleniumurl;
    }

    public static String getBrowserVersion() throws Exception {
        String browserVersion = null;
        try {
            // get the Browser value
            browserVersion = getWebProperty("Version");
        } catch (Exception e) {
            log.error(e.toString());
        }
        return browserVersion;
    }

    public static int getWaitSeconds() throws Exception {
        int waitSeconds = 0;
        try {
            // get the Browser value
            waitSeconds = Integer.parseInt(getWebProperty("Wait(Seconds)"));
        } catch (Exception e) {
            log.error(e.toString());
        }
        return waitSeconds;
    }
    private static String getWebProperty(String property) throws Exception {
        String propertyVal = null;
        // read config.properties file
        try (FileInputStream input = new FileInputStream(api.tests.BaseTests.resourceFilePath + "WebTestConfig.properties")) {
            Properties prop = new Properties();
            // load the properties file
            prop.load(input);
            // get the Web Properties value
            propertyVal = prop.getProperty(property);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return propertyVal;
    }



    @Before
    public void Initialize(Scenario scenario) throws Exception {
        System.out.println("Scenario:  " + scenario.getName());
        for(String tag : scenario.getSourceTagNames()){
        }
    }

    @After(order = 1)
    public void attachScreenshot(Scenario scenario) throws IOException {
        setScenario(scenario);
        if (getScenario().isFailed()) {
            String screenshotName = scenario.getName().replaceAll(" ", "_") + System.currentTimeMillis();
            //This takes a screenshot from the driver at save it to the specified location
            File sourcePath = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            screenshotFile = new File(CukesRunnerTest.getFolder()+ "/" + screenshotName + ".png");
            setScreenshotFile(screenshotFile);
            //Copy taken screenshot from source location to destination location
            try {
                FileUtils.copyFile(sourcePath, getScreenshotFile());
            } catch (IOException e) {
                log.error(e.toString());
            }
            //Attach screenshot to allure report - TBD
            final byte[] screenShot = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenShot, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")));
//            Allure.getLifecycle().addAttachment(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MMM-yy_hh:mm:ss")), "image/png", "png", screenShot);
        }
    }


    @After (order = 0)
    public void terminate(){
        DriverFactory.getDriver().quit();
        DriverFactory.removeDriver();
    }

}