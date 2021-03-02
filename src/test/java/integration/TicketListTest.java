package integration;

import integration.page.LoginPage;
import integration.page.TicketListPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicketListTest {
    private WebDriver webDriver;
    private TicketListPage ticketListPage;

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
        LoginPage loginPage = PageFactory.initElements(webDriver, LoginPage.class);
        loginPage.login(WebDriverConfig.getProperty("login.page.admin.user"),
                WebDriverConfig.getProperty("login.page.admin.pass"));

        new WebDriverWait(webDriver, 10)
                .until(ExpectedConditions.titleIs("Ticket List"));

        webDriver.get(WebDriverConfig.getProperty("tickets.page.url"));
        ticketListPage = new TicketListPage(webDriver);
    }

    @After
    public void after() {
        webDriver.close();
    }

    @Test
    public void ticketAdd() {
        String uniqueDescription = "ticketDescription " + UUID.randomUUID().toString();
        ticketListPage.fillNewTicketData("version 0.1", uniqueDescription, "reproduce steps");
        ticketListPage.addNewTicket();
        String ticketDescription = ticketListPage.getFirstTicketDescription();
        assertEquals(uniqueDescription, ticketDescription);
    }

    @Test
    public void ticketAddedDataCanBeCleared() {
        ticketListPage.fillNewTicketData("version 0.1", "ticket description", "reproduce steps");
        ticketListPage.clearNewTicket();
        assertTrue(ticketListPage.getFilledProductVersion().isEmpty() &&
                ticketListPage.getDescription().isEmpty() &&
                ticketListPage.getReproduceSteps().isEmpty());
    }
}