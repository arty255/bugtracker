package integration.page;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TicketListPage extends LoggedInPage {
    @FindBy(className = "addTicketDialogButton")
    private WebElement addTicketButton;
    @FindBy(className = "newTicketProductVersion")
    private WebElement productVersionField;
    @FindBy(className = "newTicketDescription")
    private WebElement descriptionField;
    @FindBy(className = "newTicketReproduceSteps")
    private WebElement reproducerStepsField;
    @FindBy(className = "saveNewTicketButton")
    private WebElement saveTicketButton;
    @FindBy(xpath = "//*[@id=\"dialogForm:j_idt107\"]")
    private WebElement clearTicketDataButton;
    @FindBy(xpath = "//*[@id=\"dialogForm:j_idt106\"]")
    private WebElement cancelSaveTicketButton;
    @FindBy(xpath = "//*[@id=\"viewForm:ticketsDataView_data\"]/tr[1]/td[2]/div[1]/span")
    private WebElement firstInRowTicketDescription;

    private WebElement sortButton;


    public TicketListPage(WebDriver webDriver) {
        super(webDriver);
    }

    public void fillNewTicketData(String productVersion, String description, String steps) {
        addTicketButton.click();
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("newTicketDialog")));
        productVersionField.sendKeys(productVersion);
        descriptionField.sendKeys(description);
        reproducerStepsField.sendKeys(steps);
    }

    public void addNewTicket() {
        saveTicketButton.click();
        waitAjaxExecution();
    }

    public void clearNewTicket() {
        clearTicketDataButton.click();
        waitAjaxExecution();
    }

    public String getFirstTicketDescription() {
        return firstInRowTicketDescription.getText();
    }

    public void latestFirstSort(){

    }

    public void waitAjaxExecution() {
        new WebDriverWait(webDriver, 5).until(d -> (boolean) ((JavascriptExecutor) d).executeScript("return jQuery.active == 0"));
    }

    public void cancelSaveTicket() {
        cancelSaveTicketButton.click();
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
}