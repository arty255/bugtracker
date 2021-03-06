package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.exception.EmptyTicketDescriptionException;
import org.hetsold.bugtracker.service.exception.EmptyUUIDKeyException;
import org.junit.After;
import org.junit.Before;
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

    private User savedUser;
    private Ticket savedTicket;

    @Before
    public void beforeTest() {
        savedUser = new User.Builder().withNames("testFN", "testLN").build();
        savedTicket = new Ticket.Builder().withData("ticket description", "steps").withCreatedBy(savedUser).build();
        Message savedMessage = new Message.Builder()
                .withCreator(savedUser)
                .withContent("test message content")
                .build();
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
        Ticket ticket = new Ticket.Builder().withUUID(null).withData("description", "steps").withCreatedBy(null).build();
        ticketService.addTicket(ticket);
    }

    @Test(expected = EmptyTicketDescriptionException.class)
    public void addTicket_emptyTicketMessageThrowException() {
        Ticket ticket = new Ticket.Builder().withUUID(null).withData("", "steps").withCreatedBy(savedUser).build();
        ticketService.addTicket(ticket);
    }

    @Test
    public void addTicket_correctSave() {
        Ticket ticket = new Ticket.Builder().withUUID(null).withData("description", "steps").withCreatedBy(savedUser).build();
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
        Ticket ticket = new Ticket.Builder().withData("description", "reproduce").withCreatedBy(savedUser).build();
        ticketService.updateTicket(ticket, savedUser);
    }

    @Test
    public void updateTicket_preformCorrect() {
        Ticket ticket = new Ticket.Builder().withUUID(savedTicket.getUuid()).withData("new Description", "steps").withCreatedBy(null).build();
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
        Ticket argument = new Ticket.Builder().withUUID(savedTicket.getUuid()).build();
        Ticket result = ticketService.getTicket(argument);
        assertEquals(savedTicket.getUuid(), result.getUuid());
    }

    @Test
    public void applyForIssue_applyCorrect() {
        ticketService.applyForIssue(savedTicket);
        assertEquals(TicketResolveState.RESOLVING, savedTicket.getResolveState());
        assertEquals(TicketVerificationState.VERIFIED, savedTicket.getVerificationState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyForIssue_emptyTicketThrowExceptionOnApplyForIssue() {
        ticketService.applyForIssue((Ticket) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void applyForIssue_notPersistedTicketThrowException() {
        Ticket ticket = new Ticket.Builder().withData("description", "steps").withCreatedBy(savedUser).build();
        ticketService.applyForIssue(ticket);
    }

    @Test
    public void addTicketMessage_correctAdd() {
        Message message = new Message.Builder()
                .withCreator(savedUser)
                .withContent("messageContent")
                .build();
        ticketService.addTicketMessage(savedTicket, message);
        Mockito.verify(messageService, Mockito.atLeastOnce()).saveNewMessage(message);
        assertTrue(savedTicket.getMessageList().contains(message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicketMessage_notPersistedTicketThrowException() {
        Message message = new Message.Builder()
                .withCreator(savedUser)
                .withContent("messageContent")
                .build();
        Ticket ticket = new Ticket.Builder().build();
        ticketService.addTicketMessage(ticket, message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addTicketMessage_nullTicketThrowException() {
        Message message = new Message.Builder()
                .withCreator(savedUser)
                .withContent("messageContent")
                .build();
        ticketService.addTicketMessage(null, message);
    }
}