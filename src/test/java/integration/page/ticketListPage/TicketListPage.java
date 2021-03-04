package integration.page.ticketListPage;

import integration.page.AbstractLoginFirstPage;
import integration.page.LoginPage;
import integration.page.PageUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class TicketListPage extends AbstractLoginFirstPage {
    private static final String ticketsUrl = "http://localhost:8080/bugtracker_war_exploded/facelets/pages/tickets.jsf";

    @FindBy(className = "addTicketDialogButton")
    private WebElement addTicketButton;
    @FindBy(xpath = "//*[@id='viewForm:ticketsDataView']")
    public TicketTableWidget ticketTable;
    @FindBy(className = "newTicketDialog")
    public AddTicketDialog addTicketDialog;

    private TicketListPage(WebDriver webDriver, String login, String password) {
        super(webDriver, login, password);
        PageFactory.initElements(webDriver, this);
        addTicketDialog = new AddTicketDialog(webDriver);
        ticketTable = new TicketTableWidget(webDriver);
    }

    public static TicketListPage createTicketListPage(WebDriver webDriver, String login, String password) {
        TicketListPage ticketListPage = new TicketListPage(webDriver, login, password);
        ticketListPage.loadPage(ticketsUrl);
        return ticketListPage;
    }

    public AddTicketDialog openTicketDialog() {
        addTicketButton.click();
        PageUtil.waitForVisibility(By.className("newTicketDialog"), webDriver);
        return addTicketDialog;
    }
}