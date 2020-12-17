package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.IssueFactory;
import org.hetsold.bugtracker.util.IssueFactoryCreatedIssueType;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class IssueServiceTest {
    @Autowired
    private IssueService issueService;
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private HistoryEventDAO historyEventDAO;
    @Autowired
    private MessageDAO messageDAO;

    private IssueFactory issueFactory;
    private MessageFactory messageFactory;
    private User user;

    @Before
    public void prepareData() {
        user = new User("test user fn", "test user ln");
        issueFactory = new IssueFactory(user);
        messageFactory = new MessageFactory(user);
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
        Mockito.verify(issueDAO).getIssueByCriteria(issue);
    }

    @Test
    @Ignore
    //not sure how to test this feature
    public void checkIfIssueCanBeLoadedByIdWithCorrectGraph() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Mockito.when(issueDAO.getIssueToDetailedViewById(issue.getUuid())).thenReturn(issue);
        Issue issueResult = issueService.getIssueForViewById(issue.getUuid());
        Mockito.verify(issueDAO).getIssueToDetailedViewById(issue.getUuid());
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
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(null);
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
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
        Mockito.verify(messageDAO).save(message);
        Mockito.verify(historyEventDAO).saveIssueMessage(Mockito.any());
    }

    @Test
    public void checkIfMessageContentCanBeUpdated() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Message newMessage = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        newMessage.setContent("new content");
        newMessage.setTitle("new title");
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(message);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueMessageContent(message, newMessage);
        Mockito.verify(messageDAO).save(message);
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(message);
        issueService.deleteMessage(message);
        Mockito.verify(messageDAO).delete(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfNotExistedMessageDeleteThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(null);
        issueService.deleteMessage(message);
        Mockito.verify(messageDAO, Mockito.never()).delete(message);
    }
}