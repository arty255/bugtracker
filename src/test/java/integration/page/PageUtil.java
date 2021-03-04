package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageUtil {
    public static void waitForPageLoad(WebDriver webDriver) {
        new WebDriverWait(webDriver, 10)
                .until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }

    public static void waitAjaxExecution(WebDriver webDriver) {
        new WebDriverWait(webDriver, 5).until(d -> ((JavascriptExecutor) d).executeScript("return jQuery.active == 0"));
        new WebDriverWait(webDriver, 5).until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
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
