package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.util.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
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
    private UserDAO userDAO;
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

    @Before
    public void prepareData() {
        MockitoAnnotations.openMocks(this);
        user = new User("test user fn", "test user ln");
        issueFactory = new IssueFactory(user);
        messageFactory = new MessageFactory(user);
        ticketFactory = new TicketFactory(user);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueDatesSaveThrowException() {
        Issue factoryIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidCreationDateIssue);
        issueService.save(factoryIssue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueUserSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidUserIssue);
        issueService.save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDescriptionAndShortDescriptionSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidDescriptionIssue);
        issueService.save(issue);
    }

    @Test
    public void checkIfCorrectIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(issue.getReportedBy().getUuid())).thenReturn(issue.getReportedBy());
        issueService.save(issue);
        Mockito.verify(issueDAO).save(issue);
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
    public void checkEmptyIssueDeleteThrowException() {
        issueService.deleteIssue(null);
    }

    @Test
    public void checkIfIssueCanBeeDeleted() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.deleteIssue(issue);
        Mockito.verify(issueDAO).delete(issue);
    }

    @Test
    public void checkIIssueSearchWithFilterPreform() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.findIssueByFilter(issue);
        Mockito.verify(issueDAO, Mockito.atLeastOnce()).getIssueByCriteria(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueStateChangeToEmptyThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setCurrentState(null);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, null, user, user);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueAssignedStateAndEmptyAssignedUserTrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issue.setAssignedTo(null);
        issueService.changeIssueState(issue, State.ASSIGNED, null, user);
    }

    @Test
    public void checkIfIssueStateStateChangeToCorrectPreform() {
        State newState = State.REOPENED;
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, newState, user, user);
        Mockito.verify(userDAO).getUserById(user.getUuid());
        Mockito.verify(issueDAO).save(issue);
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
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(null);
        issueService.addIssueMessage(issue, message, user);
    }

    @Test
    public void checkIfSaveCorrectMessagePreform() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.addIssueMessage(issue, message, user);
        Mockito.verify(historyEventDAO, Mockito.times(1)).saveIssueMessage(Mockito.any());
        Mockito.verify(messageService, Mockito.times(1)).saveMessage(message, user);
    }

    @Test
    public void checkIfIssueFromTicketTransferDataCorrect() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ArgumentCaptor<Issue> issueArgumentCaptor = ArgumentCaptor.forClass(Issue.class);
        issueService.createIssueFromTicket(ticket, ticket.getCreatedBy());
        Mockito.verify(issueDAO, Mockito.atLeastOnce()).save(issueArgumentCaptor.capture());
        Issue savedIssue = issueArgumentCaptor.getValue();
        assertEquals(ticket.getDescription(), savedIssue.getDescription());
        assertEquals(ticket.getProductVersion(), savedIssue.getProductVersion());
        assertEquals(ticket.getReproduceSteps(), savedIssue.getReproduceSteps());
        assertEquals(user, savedIssue.getReportedBy());
        assertEquals(ticket, savedIssue.getTicket());
        Mockito.verify(ticketService, Mockito.times(1)).applyForIssue(ticket);
    }


}