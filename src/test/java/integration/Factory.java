package integration;

import integration.page.LoginPage;
import integration.page.ticketListPage.TicketListPage;

public class Factory {

    public static LoginPage getLoginPage() {
        return LoginPage.createLoginPage(WebDriverConfig.getWebDriverWithOptions());
    }

    public static TicketListPage getTicketListPage() {
        return TicketListPage.createTicketListPage(
                WebDriverConfig.getWebDriverWithOptions(),
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));
    }
}