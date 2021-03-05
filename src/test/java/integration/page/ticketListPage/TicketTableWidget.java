package integration.page.ticketListPage;

import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TicketTableWidget {
    private final WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"viewForm:ticketsDataView_data\"]/tr[1]/td[2]/div[1]/span")
    private WebElement firstInRowTicketDescription;

    public TicketTableWidget(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getFirstTicketDescription() {
        PageUtil.waitForAjaxAndPage(webDriver);
        return firstInRowTicketDescription.getText();
    }

    public void sortTicketToFirstPosition() {
        By linkXpath = By.xpath("//a[text()='Creation Time']");
        webDriver.findElement(linkXpath).click();
        PageUtil.waitForAjaxAndPage(webDriver);
        waitForVisibility(linkXpath);
        webDriver.findElement(linkXpath).click();
        PageUtil.waitForAjaxAndPage(webDriver);
        waitForVisibility(linkXpath);
    }

    private void waitForVisibility(By by) {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.visibilityOfElementLocated(by));
    }
}
