package integration.page.ticketListPage;

import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AddTicketDialog {
    private final WebDriver webDriver;

    @FindBy(className = "newTicketProductVersion")
    private WebElement productVersionField;
    @FindBy(className = "newTicketDescription")
    private WebElement descriptionField;
    @FindBy(className = "newTicketReproduceSteps")
    private WebElement reproducerStepsField;
    @FindBy(className = "saveNewTicketButton")
    private WebElement saveTicketButton;
    @FindBy(className = "clearButton")
    private WebElement clearTicketDataButton;
    @FindBy(xpath = "//*[@id=\"dialogForm:j_idt106\"]")
    private WebElement cancelSaveTicketButton;

    public AddTicketDialog(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public AddTicketDialog fillNewTicketData(String productVersion, String description, String steps) {
        productVersionField.sendKeys(productVersion);
        descriptionField.sendKeys(description);
        reproducerStepsField.sendKeys(steps);
        return this;
    }

    public void saveTicket() {
        saveTicketButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
        waitDialogNotDisplayed();
    }

    public AddTicketDialog clearFilledTicketData() {
        clearTicketDataButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
        return this;
    }

    public AddTicketDialog cancelSaveTicket() {
        cancelSaveTicketButton.click();
        PageUtil.waitForAjaxAndPage(webDriver);
        waitDialogNotDisplayed();
        return this;
    }

    public String getFilledProductVersion() {
        return productVersionField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public String getReproduceSteps() {
        return reproducerStepsField.getText();
    }


    private void waitDialogNotDisplayed() {
        new WebDriverWait(webDriver, 5).until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.className("newTicketDialog"))));
    }

}
