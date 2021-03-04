package integration;

import integration.page.LoginPage;
import integration.page.detailedTicketPage.DetailedTicketPage;
import integration.page.ticketListPage.TicketListPage;

import java.util.Locale;

public class MinimalPageDataFactory {

    public static LoginPage getLoginPage() {
        return LoginPage.createLoginPage(WebDriverConfig.getWebDriverWithOptions());
    }

    public static TicketListPage getTicketListPage() {
        return TicketListPage.createTicketListPage(
                WebDriverConfig.getWebDriverWithOptions(),
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));
    }

    public static DetailedTicketPage getDetailedTicketPage(String uuid) {
        return DetailedTicketPage.createDetailedTicketPage(
                WebDriverConfig.getWebDriverWithOptions(),
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"),
                uuid
        );
    }
}