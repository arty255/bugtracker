package integration.page.detailedTicketPage;

import integration.page.AbstractLoginFirstPage;
import org.openqa.selenium.WebDriver;

public final class DetailedTicketPage extends AbstractLoginFirstPage {
    private final String detailedTicketUrl = "http://localhost:8080/bugtracker_war_exploded/facelets/pages/ticketDetail.jsf?uuid=";

    public TicketDetailWidget ticketDetailWidget;
    public PostMessageSectionWidget postMessageSectionWidget;
    public MessagesSectionWidget messagesSectionWidget;

    private DetailedTicketPage(WebDriver webDriver, String login, String password) {
        super(webDriver, login, password);
        postMessageSectionWidget = new PostMessageSectionWidget(webDriver);
        messagesSectionWidget = new MessagesSectionWidget(webDriver);
    }

    public static DetailedTicketPage createDetailedTicketPage(WebDriver webDriver, String login, String password, String ticketNumber) {
        DetailedTicketPage detailedTicketPage = new DetailedTicketPage(webDriver, login, password);
        detailedTicketPage.loadPage(detailedTicketPage.detailedTicketUrl + ticketNumber);
        return detailedTicketPage;
    }
}