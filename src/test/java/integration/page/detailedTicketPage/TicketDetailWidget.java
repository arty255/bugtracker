package integration.page.detailedTicketPage;

import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TicketDetailWidget {
    private final WebDriver webDriver;

    private WebElement ticketDescription;

    private WebElement ticketSteps;

    private WebElement ticketCreator;

    private WebElement ticketCreationDate;

    private WebElement ticketVerificationState;

    private WebElement ticketSaveButton;

    public TicketDetailWidget(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public String getTicketResolveState() {
        WebElement resolveStateElement;
        if (isTicketResolveStateEditable()) {
            resolveStateElement = webDriver.findElement(By.xpath("//div[@class='resolveStateContent']//span[@class='ui-inplace-display']"));
        } else {
            resolveStateElement = webDriver.findElement(By.xpath("//div[@class='resolveStateContent']//span[@class='coloredStateLabel']"));
        }
        return resolveStateElement.getText();
    }

    public boolean isTicketResolveStateEditable() {
        return webDriver.findElements(By.xpath("//div[@class='resolveStateContent']//span[@class='ui-inplace']")).size() > 0;
    }

    public String getTicketVerificationState() {
        return ticketVerificationState.getText();
    }

    public String getDescriptionText() {
        return ticketDescription.getText();
    }

    public void saveTicket() {
        ticketSaveButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
    }
}