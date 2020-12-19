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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class DefaultIssueServiceTest {
    @Autowired
    private IssueService issueService;
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HistoryEventDAO historyEventDAO;


    private IssueFactory issueFactory;
    private MessageFactory messageFactory;
    private TicketFactory ticketFactory;
    private User user;

    @Before
    public void prepareData() {
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
        Mockito.verify(issueDAO, Mockito.never()).save(factoryIssue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueUserSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidUserIssue);
        issueService.save(issue);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDescriptionAndShortDescriptionSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.InvalidDescriptionIssue);
        issueService.save(issue);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test
    public void checkIfCorrectIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(issue.getReportedBy().getUuid())).thenReturn(issue.getReportedBy());
        issueService.save(issue);
        Mockito.verify(issueDAO).save(issue);
    }

    @Test
    public void checkIfIssuePresentInListById() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        Issue resultIssue = issueService.getIssueById(issue.getUuid());
        Mockito.verify(issueDAO).getIssueById(issue.getUuid());
        assertEquals(issue.getUuid(), resultIssue.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDeleteThrowException() {
        issueService.deleteIssue(null);
        Mockito.verify(userDAO, Mockito.never()).delete(null);
    }

    @Test
    public void checkIfIssueCanBeeDeleted() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.deleteIssue(issue);
        Mockito.verify(issueDAO).delete(issue);
    }

    @Test
    public void checkIfCriteriaRequestPreform() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.findIssueByCriteria(issue);
        Mockito.verify(issueDAO, Mockito.atLeastOnce()).getIssueByCriteria(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueEmptyStateThrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issue.setCurrentState(null);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, null, null);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueAssignedStateAndEmptyAssignedUserTrowException() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issue.setAssignedTo(null);
        issueService.changeIssueState(issue, State.ASSIGNED, user);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test
    //@Sql(value = {"/test_db_data.sql"})
    public void checkIfIssueStateChangingCorrectly() {
        State newState = State.REOPENED;
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, newState, user);
        Mockito.verify(userDAO).getUserById(user.getUuid());
        Mockito.verify(issueDAO).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyMessageThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.IncorrectNullContentMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueService.addIssueMessage(issue, message);
        Mockito.verify(issueDAO, Mockito.never()).save(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfMessageWithNotExistedUserThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.IncorrectNullCreator);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        //Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(null);
        issueService.addIssueMessage(issue, message);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfMessageForNotExistedIssueThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(null);
        issueService.addIssueMessage(issue, message);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test
    public void checkIfCorrectMessageCanBeSaved() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.addIssueMessage(issue, message);
        Mockito.verify(historyEventDAO).saveIssueMessage(Mockito.any());
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
    }
}