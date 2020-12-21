package CucumberWeb.Pages;

import CucumberWeb.StepDefinition.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by geetha on 07-07-2019.
 */
public class BasePage {
    public static WebDriver driver;
    public static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public BasePage(WebDriver driver){
        this.driver = driver;
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void navigate(String url){
        try{
            driver.get(url);
            driver.manage().timeouts().implicitlyWait(Hooks.getWaitSeconds(), TimeUnit.SECONDS);
            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void jsclick(WebElement element){
        try{
            JavascriptExecutor executor = (JavascriptExecutor)driver;
            if (isClickable(element)) {
                executor.executeScript("arguments[0].click();", element);
            }
            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void click(WebElement element){
        try{
            if (isClickable(element)) {
                element.click();
            }
            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void VerifyElementPresent(WebElement element) {
        try{
            Boolean ElementPresent = element.isDisplayed();
            assertEquals(true,ElementPresent);
            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void setvalue(WebElement element, String value){
        try{
            if (isVisible(element)) {
                clearvalue(element);
                element.sendKeys(value);
            }

            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void clearvalue(WebElement element){
        try{
            if (isVisible(element)) {
                element.clear();
            }

            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void verifyValue(WebElement element, String value){
        try{
            assertEquals(value,element.getText());
            Thread.sleep(500);
        }catch (Exception e){
            fail(e.toString());
        }
    }

    public static void selectDropdown(WebElement element, String value) {

        try {
            if (isClickable(element)) {
                element.click();
            }
            Thread.sleep(500);
            String DrpdwnValXPath="";
            List <WebElement> dropdownValues = driver.findElements(By.xpath("//li"));
            for (WebElement webElement : dropdownValues) {
                String name = webElement.getText();
                System.out.println(name);
                if (name.equals(value)) {
                    DrpdwnValXPath="//li[contains(text(),'"+value+"')]";
                    System.out.println(name);
                    break;
                }
            }
            System.out.println("Li Size: "+driver.findElements(By.xpath("//li")).size());
            System.out.println("Elem Size: "+driver.findElements(By.xpath(DrpdwnValXPath)).size());
            if(!DrpdwnValXPath.isEmpty()) {
                if (isClickable(driver.findElement(By.xpath(DrpdwnValXPath)))) {
                    driver.findElement(By.xpath(DrpdwnValXPath)).click();
                }
            }
            Thread.sleep(500);
        } catch (Exception e) {
            fail(e.toString());
        }
    }


    public static boolean isClickable(WebElement webe)
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Hooks.getWaitSeconds());
            wait.until(ExpectedConditions.elementToBeClickable(webe));
            return true;
        }
        catch (Exception e)
        {
            fail(e.toString());
            return false;
        }
    }

    public static boolean isVisible(WebElement webe)
    {
        try
        {
            WebDriverWait wait = new WebDriverWait(driver, Hooks.getWaitSeconds());
            wait.until(ExpectedConditions.visibilityOf(webe));
            return true;
        }
        catch (Exception e)
        {
            fail(e.toString());
            return false;
        }
    }

    public static String currentDate(int days, String format) {
        //Java calendar in default timezone and default locale
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Singapore"));

        //adding days into Date in Java
        cal.add(Calendar.DATE, days);
        Date date = cal.getTime();
        switch (format.toLowerCase()) {
            case "dd/mm/yyyy":
                //return manipulated date value
                return cal.get(Calendar.DATE) + "/" +
                        (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            case "yyyy/mm/dd":
                //return manipulated date value
                return cal.get(Calendar.YEAR) + "/" +
                        (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.DATE);
            case "mm/dd/yyyy":
                //return manipulated date value
                return (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR)  ;
            case "mmm dd, yyyy":
                //return manipulated date value
                SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
                return format1.format(date);
            default:
                throw new UnsupportedOperationException("Request type is not supported.");
        }
    }

    public static String currentDate(String format) {
        //Java calendar in default timezone and default locale
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Singapore"));
        Date date = cal.getTime();

        switch (format.toLowerCase()) {
            case "dd/mm/yyyy":
                //return manipulated date value
                return cal.get(Calendar.DATE) + "/" +
                        (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            case "yyyy/mm/dd":
                //return manipulated date value
                return cal.get(Calendar.YEAR) + "/" +
                        (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.DATE);
            case "mm/dd/yyyy":
                //return manipulated date value
                return (cal.get(Calendar.MONTH) + 1) + "/" +cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR)  ;
            case "mmm dd, yyyy":
                //return manipulated date value
                SimpleDateFormat format1 = new SimpleDateFormat("MMM dd, yyyy");
                return format1.format(date);
            default:
                throw new UnsupportedOperationException("Request type is not supported.");
        }
    }

    public static void pressRight()
    {
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.delay(200);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        robot.delay(200);
    }

    public static void pressEnter()
    {
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.delay(200);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.delay(200);
    }
    public static void type(String s)
    {
        byte[] bytes = s.getBytes();
        for (byte b : bytes)
        {
            int code = b;
            // keycode only handles [A-Z] (which is ASCII decimal [65-90])
            if (code > 96 && code < 123)
                code = code - 32;
            robot.delay(40);
            robot.keyPress(code);
            robot.keyRelease(code);

        }
    }

}
