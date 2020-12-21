package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.hetsold.bugtracker.util.TicketFactory;
import org.hetsold.bugtracker.util.TicketFactoryTicketType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test"})
public class DefaultTicketServiceTest {
    @InjectMocks
    private TicketService ticketService = new DefaultTicketService();
    @Mock
    private UserDAO userDAO;
    @Mock
    private TicketDAO ticketDao;
    @Mock
    private MessageService messageService;

    private final User user = new User("First Name", "Last Name");
    private final TicketFactory ticketFactory = new TicketFactory(user);
    private MessageFactory messageFactory = new MessageFactory(user);

    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNoDescriptionCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketEmptyDescription);
        ticketService.save(ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithEmptyUserCantBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketNullCreator);
        ticketService.save(ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNotExistedUserCantBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(userDAO.getUserById(ticket.getCreatedBy().getUuid())).thenReturn(null);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.times(1)).getTicketById(ticket.getUuid());
    }

    @Test
    public void checkIfCorrectTicketCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(userDAO.getUserById(ticket.getCreatedBy().getUuid())).thenReturn(ticket.getCreatedBy());
        ticketService.save(ticket);
        Mockito.verify(ticketDao).save(ticket);
    }

    @Test
    public void checkIfTicketCanBeDeleted() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ticketService.delete(ticket);
        Mockito.verify(ticketDao).delete(ticket);
    }

    @Test
    public void checkIfTicketApplyForIssue() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(ticket);
        ticketService.applyForIssue(ticket);
        assertEquals(TicketResolveState.Resolving, ticket.getResolveState());
        assertEquals(TicketVerificationState.Verified, ticket.getVerificationState());
    }

    @Test
    public void checkIfCorrectTicketMessageWillBeAdded() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(ticket);
        ticketService.addTicketMessage(ticket, message, user);
        Mockito.verify(messageService).saveMessage(message, user);
        assertTrue(ticket.getMessageList().contains(message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketMessageWithEmptyTicketThroeException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        ticketService.addTicketMessage(null, message, user);
    }
}