package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.hetsold.bugtracker.util.TicketFactory;
import org.hetsold.bugtracker.util.TicketFactoryTicketType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test"})
public class DefaultTicketServiceTest {
    @InjectMocks
    private TicketService ticketService = new DefaultTicketService();
    @Mock
    private TicketDAO ticketDao;
    @Mock
    private MessageService messageService;
    @Mock
    private UserService userService;

    private final User user = new User("First Name", "Last Name");
    private final UserDTO userDTO = new UserDTO(user);
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
    public void checkIfTicketWithNoDescriptionThrowExceptionOnSave() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketEmptyDescription);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        ticketService.addTicket(ticketDTO, userDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNoDescriptionThrowExceptionOnUpdate() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketEmptyDescription);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        ticketService.updateTicket(ticketDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithEmptyUserThrowExceptionOnSave() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketNullCreator);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        ticketService.addTicket(ticketDTO, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNotExistedUserThrowExceptionOnSave() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        Mockito.when(userService.getUserById(userDTO.getUuid())).thenReturn(null);
        ticketService.addTicket(ticketDTO, userDTO);
    }

    @Test
    public void checkIfCorrectTicketCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        Mockito.when(userService.getUserById(userDTO.getUuid())).thenReturn(user);
        ticketService.addTicket(ticketDTO, userDTO);
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        Mockito.verify(ticketDao).save(ticketArgumentCaptor.capture());
        assertNotEquals(ticketDTO.getUuid(), ticketArgumentCaptor.getValue().getUuid());
    }

    @Test
    public void checkIfCorrectTicketCanBeUpdated() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        ticketDTO.setDescription("changed");
        Mockito.when(userService.getUserById(userDTO.getUuid())).thenReturn(user);
        Mockito.when(ticketDao.getTicketById(ticketDTO.getUuid())).thenReturn(ticket);
        TicketDTO savedDto = ticketService.updateTicket(ticketDTO);
        assertEquals(ticketDTO.getDescription(), savedDto.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithEmptyUUIDThrowException() {
        ticketService.delete("");
    }

    @Test
    public void checkIfTicketCanBeDeleted() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(ticket);
        ticketService.delete(ticket.getUuid());
        Mockito.verify(ticketDao).delete(ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfGetTicketByEmptyIdThrowException() {
        ticketService.getTicketById("");
    }

    @Test
    public void checkIfTicketApplyForIssue() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(ticket);
        ticketService.applyForIssue(ticket);
        assertEquals(TicketResolveState.Resolving, ticket.getResolveState());
        assertEquals(TicketVerificationState.Verified, ticket.getVerificationState());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfNotExistedTicketThrowExceptionOnApplyForIssue() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(null);
        ticketService.applyForIssue(ticket);
    }

    @Test
    public void checkIfCorrectTicketMessageWillBeAdded() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(ticket);
        Mockito.when(messageService.saveOrUpdateMessage(message, user)).thenReturn(message);
        ticketService.addTicketMessage(ticket, message, user);
        Mockito.verify(messageService).saveOrUpdateMessage(message, user);
        assertTrue(ticket.getMessageList().contains(message));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfAddTicketMessageToNotExistedTicketThrowException() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(null);
        ticketService.addTicketMessage(ticket, message, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfAddTicketMessageWithNullTicketThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        ticketService.addTicketMessage(null, message, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfMessagesForNullTicketThrowException() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        Mockito.when(ticketDao.getTicketById(ticket.getUuid())).thenReturn(null);
        ticketService.getTicketMessages(ticketDTO, 0, 0, false);
    }
}