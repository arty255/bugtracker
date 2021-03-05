package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class PageUtil {
    public static void waitForPageLoad(WebDriver webDriver) {
        new WebDriverWait(webDriver, 10)
                .until(driver -> "complete".equals(((JavascriptExecutor) driver).executeScript("return document.readyState")));
    }

    public static void waitForJQuery(WebDriver webDriver) {
        new WebDriverWait(webDriver, 5).until(d -> (boolean)((JavascriptExecutor) d).executeScript("return jQuery.active == 0"));
    }

    public static void waitForMinimalAnimation(WebDriver webDriver) {
    }

    public static void waitForAjaxAndPage(WebDriver webDriver) {
        waitForPageLoad(webDriver);
        waitForJQuery(webDriver);
        waitForMinimalAnimation(webDriver);
    }

    public static void waitSeconds(WebDriver webDriver, int sec) {
        webDriver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }

    public static void waitForVisibility(WebElement element, WebDriver webDriver) {
        new WebDriverWait(webDriver, 5)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForVisibility(By locator, WebDriver webDriver) {
        new WebDriverWait(webDriver, 5)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
