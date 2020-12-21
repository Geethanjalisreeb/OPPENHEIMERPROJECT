package CucumberWeb.StepDefinition;

import CucumberWeb.Pages.HomePage;
import CucumberWeb.SeleniumConfig.SharedDriver;
import api.constants.RequestType;
import api.helpers.RestAssuredHelper;
import api.tests.BaseTests;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import javafx.beans.property.ReadOnlyStringProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static CucumberWeb.Pages.BasePage.*;
import static org.junit.Assert.fail;

public class UICucumberSteps extends BaseStepClass {

    public UICucumberSteps(SharedDriver driver) throws Exception {
        super(driver);
    }

   @Given("^I am Navigating to Oppenheimer Home Page$")
    public void i_am_Navigating_to_Home_Page() throws Exception {
        BaseTests baseTests=new BaseTests();
        RestAssuredHelper restAssuredHelper=new RestAssuredHelper();
        restAssuredHelper.getResponse(RequestType.Post,"/calculator/rakeDatabase");
        navigate(Hooks.getUrl());
        VerifyElementPresent(homePage.HomePageText);
    }

    @Given("I Navigate to url, {string}")
    public void iNavigateToUrl(String arg0) {
    }

    @When("I upload file as a clerk")
    public void iUploadFileAsAClerk() throws InterruptedException, AWTException {
        Actions builder = new Actions(driver);
        Action mouseOverHome = builder
                .moveToElement(homePage.FileInput)
                .click()
                .build();
        mouseOverHome.perform();

        // Cmd + Tab is needed since it launches a Java app and the browser looses focus

        robot.keyPress(KeyEvent.VK_META);

        robot.keyPress(KeyEvent.VK_TAB);

        robot.keyRelease(KeyEvent.VK_META);

        robot.keyRelease(KeyEvent.VK_TAB);

        robot.delay(500);
        pressRight();
        type("Dow");
        pressRight();
        type("Test");
        pressEnter();


        click(homePage.RefreshTaxReliefBtn);

        Thread.sleep(3000);
    }


    @Then("I should see the below values are displayed in the table")
    public void iShouldSeeTheBelowValuesAreDisplayedInTheTable(DataTable dataTable) {
        List <List <String>> explist = dataTable.asLists(String.class);
        System.out.println("Expectedlist:"+explist);
        List<List<String>> actlist = new ArrayList <List<String>>();

        java.util.List <WebElement> rows_table = homePage.TaxReliefTable.findElements(By.tagName("tr"));
        //To calculate no of rows In table.
        int rows_count = rows_table.size();
        System.out.println("Row:"+rows_count);
        //Loop will execute till the last row of table.
        for (int row = 1; row < rows_count; row++) {
            List<String> values = new ArrayList <String>();
            //To locate columns(cells) of that specific row.
            WebElement natIDelem = homePage.TaxReliefTable.findElement(By.xpath("//tbody/tr["+row+"]/td[1]"));
            WebElement ReliefAmountelem = homePage.TaxReliefTable.findElement(By.xpath("//tbody/tr["+row+"]/td[2]"));
            String natID = natIDelem.getText();
            String ReliefAmount = ReliefAmountelem.getText();
            values.add(natID);
            values.add(ReliefAmount);
            actlist.add(values);
        }

        System.out.println("Actuallist:"+actlist);

        for(List<String> list:actlist){
            Assert.assertTrue(explist.contains(list));
       }

    }

    @Then("verify text and color of the dispense button is {string} and {string} respectively")
    public void verifyTextAndColorOfTheDispenseButtonIsAndRespectively(String arg0, String arg1) {
        Assert.assertEquals(cashDispensePage.Btn_DispenseNow.getText(),arg0);
        Assert.assertEquals(cashDispensePage.Btn_DispenseNow.getCssValue("background-color"),arg1);
    }

    @Then("I should see {string} message is displayed")
    public void iShouldSeeMessageIsDisplayed(String arg0) {
        Assert.assertEquals(cashDispensePage.Text_CashDispensed.getText(),arg0);
    }

    @When("I click on Dispense Now button")
    public void iClickOnDispenseNowButton() {
        click(cashDispensePage.Btn_DispenseNow);
    }

    @And("Verify total amount and working class hero count are displayed as {string} and {string} respectively")
    public void verifyTotalAmountAndWorkingClassHeroCountAreDisplayedAsAndRespectively(String arg0, String arg1) {
        WebElement taxSummaryElem = driver.findElement(By.xpath("//p[contains(.,'£"+arg0+" will be dispensed to "+arg1+" Working Class Hero/s')]"));
        VerifyElementPresent(taxSummaryElem);
    }
}
