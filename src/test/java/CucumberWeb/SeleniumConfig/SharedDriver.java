package CucumberWeb.SeleniumConfig;

import CucumberWeb.StepDefinition.Hooks;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class SharedDriver {

    private WebDriver driver;
    private static String OS = System.getProperty("os.name").toLowerCase();
    private DesiredCapabilities capability = new DesiredCapabilities();


    public SharedDriver() throws Exception {
        if (DriverFactory.getDriver() == null) {
            if (Hooks.getZaleniumEnabled().toUpperCase().trim().equals("YES")){
                capability.setCapability(CapabilityType.BROWSER_NAME, Hooks.getBrowser());
                driver = new RemoteWebDriver(new URL(Hooks.getZaleniumURL()), capability);
            }else{
                driver = standaloneDriver(Hooks.getBrowser());
            }
            assert driver != null;
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            DriverFactory.addDriver(driver);
        }
    }

    public WebDriver standaloneDriver(String browser) throws Exception {

        String currentDir   =   System.getProperty("user.dir");
        String driverPath   =   currentDir + "/DriverFiles/";

        switch(browser.toUpperCase()){

            case "CHROME":
                if (OS.contains("win")) {
                    System.setProperty("webdriver.chrome.driver", driverPath+"chromedriver"+Hooks.getBrowserVersion()+".exe");
                } else if (OS.contains("mac")){
                    System.setProperty("webdriver.chrome.driver", driverPath+"chromedriver"+Hooks.getBrowserVersion());
                }
                capability = DesiredCapabilities.chrome();
                capability.setBrowserName("chrome");
                driver = new ChromeDriver(capability);
                break;

            case "FIREFOX":
                if (OS.contains("win")) {
                    System.setProperty("webdriver.gecko.driver", driverPath+"geckodriver"+Hooks.getBrowserVersion()+".exe");
                } else if (OS.contains("mac")){
                    System.setProperty("webdriver.gecko.driver", driverPath+"geckodriver"+Hooks.getBrowserVersion());
                }
                driver = new FirefoxDriver();
                break;

        }
        return driver;
    }
}
