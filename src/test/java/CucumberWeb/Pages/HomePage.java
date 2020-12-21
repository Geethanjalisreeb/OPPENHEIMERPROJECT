package CucumberWeb.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import static org.junit.Assert.*;


/**
 * Created by geetha on 07-07-2019.
 */
public class HomePage extends BasePage {

    @FindBy(xpath = "//input[@type='file']")
    public WebElement FileInput;
    @FindBy(xpath = "//h1[contains(.,'The Oppenheimer Project')]")
    public WebElement HomePageText;
    @FindBy(xpath = "//button[@type='button'][contains(.,'Refresh Tax Relief Table')]")
    public WebElement RefreshTaxReliefBtn;
    @FindBy(xpath = "//table[@class='table table-hover table-dark']")
    public WebElement TaxReliefTable;


    public HomePage(WebDriver driver) {
        super(driver);
    }


}
