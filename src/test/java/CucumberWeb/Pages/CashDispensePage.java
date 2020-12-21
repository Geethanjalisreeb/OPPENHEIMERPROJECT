package CucumberWeb.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


/**
 * Created by geetha on 07-07-2019.
 */
public class CashDispensePage extends BasePage {


    @FindBy(xpath = "//a[contains(@class,'btn btn-danger btn-block')]")
    public  WebElement Btn_DispenseNow;
    @FindBy(xpath = "//div[contains(@class,'display-4 font-weight-bold')]")
    public  WebElement Text_CashDispensed;

    public CashDispensePage(WebDriver driver) {
        super(driver);
    }
}