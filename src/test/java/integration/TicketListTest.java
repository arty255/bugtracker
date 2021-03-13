package integration;

import integration.page.ticketListPage.TicketListPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TicketListTest {
    private TicketListPage ticketListPage;

    @BeforeClass
    public static void beforeClass() {
        WebDriverConfig.init();
    }

    @After
    public void after() {
        ticketListPage.closeWebDriver();
    }

    @Before
    public void before() {
        ticketListPage = MinimalPageDataFactory.getTicketListPage();
    }

    @Test
    public void ticketAdd() {
        String uniqueDescription = "ticketDescription " + UUID.randomUUID().toString();
        ticketListPage
                .openTicketDialog()
                .fillNewTicketData("version 0.1", uniqueDescription, "reproduce steps")
                .saveTicket();
        ticketListPage
                .ticketTable
                .sortTicketToFirstPosition();
        String ticketDescription = ticketListPage.ticketTable.getFirstTicketDescription();
        assertEquals("added ticket contains different data", uniqueDescription, ticketDescription);
    }

    @Test
    public void ticketAddedDataCanBeCleared() {
        ticketListPage
                .openTicketDialog()
                .fillNewTicketData("version 0.1", "ticket description", "reproduce steps")
                .clearFilledTicketData();
        assertTrue("entered ticket data not cleared", ticketListPage.addTicketDialog.getFilledProductVersion().isEmpty() &&
                ticketListPage.addTicketDialog.getDescription().isEmpty() &&
                ticketListPage.addTicketDialog.getReproduceSteps().isEmpty());
    }


}