package integration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoggedInPage {
    private WebDriver webDriver;
    @FindBy(xpath = "//*[@id=\"j_idt11:j_idt12\"]/ul/li[5]")
    private WebElement welcomeMessage;
    @FindBy(xpath = "//*[@id=\"j_idt11:j_idt12\"]/ul/li[1]/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/tickets.jsf')]")
    private WebElement ticketMenuItem;
    @FindBy(xpath = "//*[@id=\"j_idt11:j_idt12\"]/ul/li[2]/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/issues.jsf')]")
    private WebElement issuesMenuItem;
    @FindBy(xpath = "//*[@id=\"j_idt11:j_idt12\"]/ul/li[3]/a[contains(@href,'/bugtracker_war_exploded/facelets/pages/users.jsf')]")
    private WebElement usersMenuItem;

    public LoggedInPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public String getWelcomeMessage() {
        return welcomeMessage.getText();
    }

    public boolean containsTicketMenuItem() {
        return ticketMenuItem != null;
    }

    public boolean containsIssuesMenuItem() {
        return issuesMenuItem != null;
    }

    public boolean containsUsersMenuItem() {
        return usersMenuItem != null;
    }
}