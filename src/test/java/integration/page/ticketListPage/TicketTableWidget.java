package integration.page.ticketListPage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TicketTableWidget {
    private WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"viewForm:ticketsDataView_data\"]/tr[1]/td[2]/div[1]/span")
    private WebElement firstInRowTicketDescription;

    public TicketTableWidget(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getFirstTicketDescription() {
        waitAjaxExecution();
        return firstInRowTicketDescription.getText();
    }

    public void sortTicketToFirstPosition() {
        webDriver.findElement(By.xpath("//a[text()='Creation Time']")).click();
        waitAjaxExecution();
        waitForVisibility(By.xpath("//a[text()='Creation Time']"));
        webDriver.findElement(By.xpath("//a[text()='Creation Time']")).click();
        waitAjaxExecution();
        waitForVisibility(By.xpath("//a[text()='Creation Time']"));
    }

    private void waitForVisibility(By by) {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private void waitAjaxExecution() {
        new WebDriverWait(webDriver, 5).until(d -> ((JavascriptExecutor) d).executeScript("return jQuery.active == 0"));
        new WebDriverWait(webDriver, 5).until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
    }
}
