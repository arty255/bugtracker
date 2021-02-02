package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dto.*;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.util.*;
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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
public class DefaultIssueServiceTest {
    @InjectMocks
    private final IssueService issueService = new DefaultIssueService();
    @Mock
    private IssueDAO issueDAO;
    @Mock
    private UserService userService;
    @Mock
    private HistoryEventDAO historyEventDAO;
    @Mock
    private MessageService messageService;
    @Mock
    private TicketService ticketService;

    private IssueFactory issueFactory;
    private MessageFactory messageFactory;
    private TicketFactory ticketFactory;
    private User user;
    private UserDTO userDTO;

    @Before
    public void prepareData() {
        MockitoAnnotations.openMocks(this);
        user = new User("Alex", "Tester");
        userDTO = new UserDTO(user);
        issueFactory = new IssueFactory(user);
        messageFactory = new MessageFactory(user);
        ticketFactory = new TicketFactory(user);
        issueService.setStateValidationStrategy(new DefaultIssueStateValidationStrategy());
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueUserThrowExceptionOnSave() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidUserIssue);
        IssueDTO issueDTO = new IssueDTO(issue);
        Mockito.when(userService.getUserById(issue.getUuid())).thenReturn(null);
        issueService.saveOrUpdateIssue(issueDTO, userDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDescriptionThrowExceptionOnSave() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidDescriptionIssue);
        issue.setDescription("");
        IssueDTO issueDTO = new IssueDTO(issue);
        Mockito.when(userService.getUserById(issue.getUuid())).thenReturn(user);
        issueService.saveOrUpdateIssue(issueDTO, userDTO);
    }

    @Test
    public void checkIfCorrectIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setUuid("");
        IssueDTO issueDTO = new IssueDTO(issue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        issueService.saveOrUpdateIssue(issueDTO, userDTO);
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        Mockito.verify(issueDAO).save(issueArgumentCaptor.capture());
        assertEquals(issueDTO.getDescription(), issueArgumentCaptor.getValue().getDescription());
    }

    @Test
    public void checkIfCorrectIssueCanBeUpdated() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setDescription("changed");
        IssueDTO issueDTO = new IssueDTO(issue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        IssueDTO savedIssue = issueService.saveOrUpdateIssue(issueDTO, userDTO);
        assertEquals(issue.getUuid(), savedIssue.getUuid());
        assertEquals(issue.getDescription(), savedIssue.getDescription());
    }

    @Test
    public void checkIfCorrectIssueWillChangeArchiveState() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Issue editableIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        editableIssue.setArchived(true);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(editableIssue);
        issueService.changeIssueArchiveState(issue, user, false);
        assertEquals(false, editableIssue.getArchived());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIncorrectIssueThrowExceptionOnChangeIssueArchivedState() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Issue editableIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        editableIssue.setArchived(true);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(null);
        issueService.changeIssueArchiveState(issue, user, false);
    }

    @Test
    public void checkIfIssueCanByFoundById() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        Issue resultIssue = issueService.getIssueById(issue.getUuid());
        Mockito.verify(issueDAO).getIssueById(issue.getUuid());
        assertEquals(issue.getUuid(), resultIssue.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueThrowExceptionByNullIdSearch() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setUuid("");
        issueService.getIssueById(issue.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDeleteThrowException() {
        issueService.deleteIssue(null);
    }

    @Test
    public void checkIfIssueCanBeeDeleted() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.deleteIssue(issue);
        Mockito.verify(issueDAO).delete(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueStateChangeToEmptyThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setCurrentIssueState(null);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, null, user);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test
    public void checkIfIssueStateChangeToCorrectCanBePreform() {
        IssueState newIssueState = IssueState.REOPEN;
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, newIssueState, user);
        assertEquals(issue.getCurrentIssueState(), newIssueState);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueAssignStateThrowException() {
        IssueState newIssueState = IssueState.ASSIGNED;
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, newIssueState, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfWrongAssignedUserThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(null);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueAssignedUser(issue, user, user);
    }

    @Test
    @Ignore
    /*assignation concept unclear*/
    public void checkIfAssignedUserAlsoChangeIssueSate() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueAssignedUser(issue, user, user);
        assertEquals(issue.getCurrentIssueState(), IssueState.ASSIGNED);
        assertEquals(issue.getAssignedTo(), user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfSaveEmptyIssueMessageThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.IncorrectNullContentMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.addIssueMessage(issue, message, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfSaveMessageWithNotExistedUserThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.IncorrectNullCreator);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.addIssueMessage(issue, message, user);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfSaveMessageForNotExistedIssueThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(null);
        issueService.addIssueMessage(issue, message, user);
    }

    @Test
    public void checkIfSaveCorrectMessagePreform() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.addIssueMessage(issue, message, user);
        Mockito.verify(historyEventDAO, Mockito.times(1)).saveIssueMessage(Mockito.any());
        Mockito.verify(messageService, Mockito.times(1)).saveNewMessage(message, user);
    }

    @Test
    public void checkIfIssueMessageCanBeDeleted() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        MessageDTO messageDTO = new MessageDTO(message);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        Mockito.when(messageService.getMessageById(message.getUuid())).thenReturn(message);
        issueService.deleteIssueMessage(messageDTO);
        ArgumentCaptor<IssueMessageEvent> messageEventArgumentCaptor = ArgumentCaptor.forClass(IssueMessageEvent.class);
        Mockito.verify(historyEventDAO, Mockito.times(1)).deleteIssueMessageEvent(messageEventArgumentCaptor.capture());
        Mockito.verify(messageService).delete(message);
    }

    @Test
    public void checkIfIssueCanBeCreatedFromTicket() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(ticketService.getTicketById(ticket.getUuid())).thenReturn(ticket);
        issueService.createIssueFromTicket(ticket, user);
        Mockito.verify(issueDAO, Mockito.atLeastOnce()).save(issueArgumentCaptor.capture());
        Issue savedIssue = issueArgumentCaptor.getValue();
        assertEquals(ticket.getDescription(), savedIssue.getDescription());
        assertEquals(ticket.getProductVersion(), savedIssue.getProductVersion());
        assertEquals(ticket.getReproduceSteps(), savedIssue.getReproduceSteps());
        assertEquals(user, savedIssue.getReportedBy());
        assertEquals(ticket, savedIssue.getTicket());
        Mockito.verify(ticketService, Mockito.times(1)).applyForIssue(ticket);
    }

    @Test
    public void checkIfIssueCanBeLinkedToTicket() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        IssueShortDTO issueShortDTO = new IssueShortDTO(issue);
        Mockito.when(issueDAO.getIssueById(issueShortDTO.getUuid())).thenReturn(issue);
        Mockito.when(ticketService.getTicketById(ticketDTO.getUuid())).thenReturn(ticket);
        issueService.assignIssueToTicket(issueShortDTO, ticketDTO);
        assertEquals(issue.getTicket(), ticket);
    }

    @Test
    public void checkIfNullIssueCanBeLinkedToTicket() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Issue linkedIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        ticket.setIssue(linkedIssue);
        ticket.getIssue().setTicket(ticket);
        TicketDTO ticketDTO = new TicketDTO(ticket);
        Mockito.when(ticketService.getTicketById(ticketDTO.getUuid())).thenReturn(ticket);
        issueService.assignIssueToTicket(null, ticketDTO);
        assertNull(linkedIssue.getTicket());
    }

    @Test
    public void checkIfNullTicketCanBeLinkedToIssue() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        IssueShortDTO issueShortDTO = new IssueShortDTO(issue);
        Mockito.when(issueDAO.getIssueById(issueShortDTO.getUuid())).thenReturn(issue);
        issueService.assignIssueToTicket(issueShortDTO, null);
        assertNull(issue.getTicket());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfNullIssueAndNullTicketThrowExceptionOnLinking() {
        issueService.assignIssueToTicket(null, null);
    }
}