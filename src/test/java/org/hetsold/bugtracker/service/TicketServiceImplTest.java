package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.exception.EmptyTicketDescriptionException;
import org.hetsold.bugtracker.service.exception.EmptyUUIDKeyException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test"})
public class TicketServiceImplTest {
    @Mock
    private TicketDAO ticketDao;
    @Mock
    private MessageServiceImpl messageService;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private final TicketServiceImpl ticketService = new TicketServiceImpl();

    private final User savedUser = new User("testFN", "testLN");
    private final Ticket savedTicket = new Ticket("ticket description", "steps", savedUser);
    private final Message savedMessage = new Message(savedUser, "test message content");

    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(userService.getUser(savedUser)).thenReturn(savedUser);
        Mockito.when(ticketDao.getTicketById(savedTicket.getUuid())).thenReturn(savedTicket);
        Mockito.when(messageService.getMessage(savedMessage)).thenReturn(savedMessage);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicket_emptyTicketThrowException() {
        ticketService.addTicket((Ticket) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicket_emptyTicketCreatorThrowException() {
        Ticket ticket = new Ticket(null, "description", "steps", null);
        ticketService.addTicket(ticket);
    }

    @Test(expected = EmptyTicketDescriptionException.class)
    public void addTicket_emptyTicketMessageThrowException() {
        Ticket ticket = new Ticket(null, "", "steps", savedUser);
        ticketService.addTicket(ticket);
    }

    @Test
    public void addTicket_correctSave() {
        Ticket ticket = new Ticket(null, "description", "steps", savedUser);
        ticketService.addTicket(ticket);
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        Mockito.verify(ticketDao).save(ticketArgumentCaptor.capture());
        assertEquals(ticket.getDescription(), ticketArgumentCaptor.getValue().getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTicket_emptyTicketThrowException() {
        ticketService.updateTicket(null, savedUser);
    }

    @Test(expected = EmptyTicketDescriptionException.class)
    public void updateTicket_emptyTicketDescriptionThrowException() {
        savedTicket.setDescription("");
        ticketService.updateTicket(savedTicket, savedUser);
    }

    @Test(expected = EmptyUUIDKeyException.class)
    public void updateTicket_emptyTicketUUIDThrowException() {
        savedTicket.setUuid(null);
        ticketService.updateTicket(savedTicket, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTicket_NotPersistedTicketThrowException() {
        Ticket ticket = new Ticket("description", "reproduce", savedUser);
        ticketService.updateTicket(ticket, savedUser);
    }

    @Ignore //not fully implemented - ticket dont have editor field
    @Test(expected = IllegalArgumentException.class)
    public void updateTicket_NotPersistedTicketEditorThrowException() {
        Ticket ticket = new Ticket(savedTicket.getUuid(), "description", "reproduce", savedUser);
        User editor = new User("test", "test");
        ticketService.updateTicket(ticket, editor);
    }

    @Test
    public void updateTicket_preformCorrect() {
        Ticket ticket = new Ticket(savedTicket.getUuid(), "new Description", "reproduce", savedUser);
        ticketService.updateTicket(ticket, savedUser);
        assertEquals("new Description", savedTicket.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTicket_EmptyTicketThrowException() {
        ticketService.delete((Ticket) null);
    }

    @Test()
    public void deleteTicket_deleteCorrect() {
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        ticketService.delete(savedTicket);
        Mockito.verify(ticketDao).delete(ticketArgumentCaptor.capture());
        assertEquals(savedTicket.getUuid(), ticketArgumentCaptor.getValue().getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTicket_emptyTicketThrowException() {
        ticketService.getTicket(null);
    }

    @Test
    public void getTicket_returnCorrect() {
        Ticket argument = new Ticket(savedTicket.getUuid());
        Ticket result = ticketService.getTicket(argument);
        assertEquals(savedTicket.getUuid(), result.getUuid());
    }

    @Test
    public void applyForIssue_applyCorrect() {
        ticketService.applyForIssue(savedTicket);
        assertEquals(TicketResolveState.Resolving, savedTicket.getResolveState());
        assertEquals(TicketVerificationState.Verified, savedTicket.getVerificationState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyForIssue_emptyTicketThrowExceptionOnApplyForIssue() {
        ticketService.applyForIssue((Ticket) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyForIssue_notPersistedTicketThrowException() {
        Ticket ticket = new Ticket("description", "steps", savedUser);
        ticketService.applyForIssue(ticket);
    }

    @Test
    public void addTicketMessage_correctAdd() {
        Message message = new Message(savedUser, "messageContent");
        ticketService.addTicketMessage(savedTicket, message);
        Mockito.verify(messageService, Mockito.atLeastOnce()).saveNewMessage(message);
        assertTrue(savedTicket.getMessageList().contains(message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicketMessage_notPersistedTicketThrowException() {
        Message message = new Message(savedUser, "messageContent");
        Ticket ticket = new Ticket();
        ticketService.addTicketMessage(ticket, message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicketMessage_nullTicketThrowException() {
        Message message = new Message(savedUser, "messageContent");
        ticketService.addTicketMessage(null, message);
    }
}