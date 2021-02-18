package integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest {
    private static WebDriver webDriver;
    private LoginPage loginPage;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", WebDriverConfig.getProperty("webdriver.chrome.driver"));
        webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        webDriver.get(WebDriverConfig.getProperty("login.page.url"));
    }

    @Before
    public void before() {
        loginPage = new LoginPage(webDriver);
    }

    @Test
    public void login_adminLogin() {
        LoggedInPage loggedInPage = loginPage.login(
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));
        assertTrue(loggedInPage.getWelcomeMessage().contains("Hi"));
        assertTrue(loggedInPage.containsTicketMenuItem() &&
                loggedInPage.containsIssuesMenuItem() &&
                loggedInPage.containsUsersMenuItem());

    }

    @Test
    public void login_wrongUserMessageAppears() {
        loginPage.login(WebDriverConfig.getProperty("login.page.admin.user"), "test11");
        assertEquals("No User with that login or password", loginPage.errorMessage());
    }
}