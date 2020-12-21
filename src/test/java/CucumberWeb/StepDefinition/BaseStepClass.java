package CucumberWeb.StepDefinition;
import CucumberWeb.Pages.*;
import CucumberWeb.SeleniumConfig.DriverFactory;
import CucumberWeb.SeleniumConfig.SharedDriver;
import org.openqa.selenium.support.PageFactory;


/**
 * Created by geetha on 07-07-2019.
 */
public class BaseStepClass {
    protected HomePage homePage;
    protected CashDispensePage cashDispensePage;


    public BaseStepClass(SharedDriver driver) throws Exception {
        PageFactory.initElements(DriverFactory.getDriver(), BasePage.class);
        homePage = PageFactory.initElements(DriverFactory.getDriver(), HomePage.class);
        cashDispensePage = PageFactory.initElements(DriverFactory.getDriver(), CashDispensePage.class);
    }

}
