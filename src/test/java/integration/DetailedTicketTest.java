package integration;

import integration.page.detailedTicketPage.DetailedTicketPage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class DetailedTicketTest {
    private DetailedTicketPage detailedTicketPage;

    @BeforeClass
    public static void beforeClass() {
        WebDriverConfig.init();
    }

    @Before
    public void before() {
        detailedTicketPage = MinimalPageDataFactory.getDetailedTicketPage("030c1546-f0b8-40cf-ab12-1423861129be");
    }

    @After
    public void after() {
        detailedTicketPage.closeWebDriver();
    }

    @Test
    public void messageCanBeAdded() {
        String postedMessageContent = "message content " + UUID.randomUUID().toString();
        detailedTicketPage
                .postMessageSectionWidget
                .expand()
                .selectWrite()
                .fillMessage(postedMessageContent)
                .sendMessage();
        String fetchedMessageContent = detailedTicketPage
                .messagesSectionWidget
                .sortByDate()
                .getMessageContent(1);
        assertEquals("message content not equals", postedMessageContent, fetchedMessageContent);
    }

    @Test
    public void filledDataCanBeCleared() {
        String testMessage = "message content";
        detailedTicketPage
                .postMessageSectionWidget
                .expand()
                .selectWrite()
                .fillMessage(testMessage)
                .clearMessage();
        assertEquals("message is not clear", "", detailedTicketPage.postMessageSectionWidget.getFilledMessageContent());
    }

    @Test
    public void previewContainsFilledData() {
        String messageContent = "message content " + UUID.randomUUID().toString();
        detailedTicketPage
                .postMessageSectionWidget
                .expand()
                .selectWrite()
                .fillMessage(messageContent)
                .selectPreview();
        assertEquals(messageContent, detailedTicketPage.postMessageSectionWidget.getPreviewContent());
    }

    @Test
    public void messagePostWidgetCanBeCollapsed() {
        detailedTicketPage
                .postMessageSectionWidget
                .expand()
                .hideCommentWindow();
        assertFalse(detailedTicketPage.postMessageSectionWidget.isExpanded());
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        detailedTicketPage
                .postMessageSectionWidget
                .fillMessage("added message")
                .sendMessage();
        String messageID = detailedTicketPage
                .messagesSectionWidget
                .getMessageUUID(1);
        detailedTicketPage
                .messagesSectionWidget
                .deleteMessage(1);
        assertNotEquals(messageID, detailedTicketPage.messagesSectionWidget.getMessageUUID(1));
    }
}