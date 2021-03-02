package integration;

import integration.page.LoggedInPage;
import integration.page.LoginPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginTest {
    private WebDriver webDriver;
    private LoginPage loginPage;

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("webdriver.chrome.driver", WebDriverConfig.getProperty("webdriver.chrome.driver"));
    }

    @Before
    public void before() {
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        webDriver = new ChromeDriver(options);
        webDriver.manage().window().maximize();
        webDriver.get(WebDriverConfig.getProperty("login.page.url"));
        loginPage = new LoginPage(webDriver);
    }

    @Test
    public void adminLogin() {
        LoggedInPage loggedInPage = loginPage.login(
                WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id, 'userGreetingsText')]")));
        assertTrue(loggedInPage.getWelcomeMessage().contains("Hi"));
        assertTrue(loggedInPage.containsTicketMenuItem() &&
                loggedInPage.containsIssuesMenuItem() &&
                loggedInPage.containsUsersMenuItem());
    }

    @Test
    public void wrongUserMessageAppearsOnLogin() {
        loginPage.login(WebDriverConfig.getProperty("login.page.admin.user"), "test11");
        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("ui-messages-error")));
        assertEquals("No User with that login or password", loginPage.errorMessage());
    }

    @After
    public void after() {
        webDriver.close();
    }
}