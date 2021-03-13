package integration;

import integration.page.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LoginTest {
    private LoginPage loginPage;

    @BeforeClass
    public static void beforeClass() {
        WebDriverConfig.init();
    }

    @After
    public void after() {
        loginPage.closeWebDriver();
    }

    @Before
    public void before() {
        loginPage = MinimalPageDataFactory.getLoginPage();
    }

    @Test
    public void adminLogin() {
        loginPage.login(
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));
        /*assertTrue(loggedInPage.getWelcomeMessage().contains("Hi"));
        assertTrue(loggedInPage.containsTicketMenuItem() &&
                loggedInPage.containsIssuesMenuItem() &&
                loggedInPage.containsUsersMenuItem());*/
    }

    @Test
    public void wrongUserMessageAppearsOnLogin() {
        loginPage.login(WebDriverConfig.getProperty("login.page.admin.user"), "test11");
        assertTrue(loginPage.containsErrorMessage());
    }
}