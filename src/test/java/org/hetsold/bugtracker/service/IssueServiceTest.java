package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.HistoryEventDAO;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;
import org.junit.Before;
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


    IssueFactory issueFactory;

    @Before
    public void prepareData() {
        User firstUser = new User("reportUser1", "report user1");
        User secondUser = new User("reportUser2", "report user2");
        issueFactory = new IssueFactory(firstUser, secondUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueDatesSaveThrowException() {
        Issue factoryIssue = issueFactory.getIssue(IssueType.InvalidCreationDateIssue);
        issueService.save(factoryIssue);
        Mockito.verify(issueDAO, Mockito.never()).save(factoryIssue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueUserSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueType.InvalidUserIssue);
        issueService.save(issue);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDescriptionAndShortDescriptionSaveThrowException() {
        Issue issue = issueFactory.getIssue(IssueType.InvalidShortAndFullDescriptionIssue);
        issueService.save(issue);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test
    public void checkIfCorrectIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(issue.getReportedBy().getUuid())).thenReturn(issue.getReportedBy());
        issueService.save(issue);
        Mockito.verify(issueDAO).save(issue);
    }

    @Test
    public void checkIfIssuePresentInListById() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
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
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        issueService.deleteIssue(issue);
        Mockito.verify(issueDAO).delete(issue);
    }

    @Test
    public void checkIfCriteriaRequestPreform() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        issueService.findIssueByCriteria(issue);
        Mockito.verify(issueDAO).getIssueByCriteria(issue);
    }

    @Test
    public void checkIfIssueCanBeLoadedByIdWithCorrectGraph() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        Mockito.when(issueDAO.getIssueToDetailedViewById(issue.getUuid())).thenReturn(issue);
        Issue issueResult = issueService.getIssueForViewById(issue.getUuid());
        Mockito.verify(issueDAO).getIssueToDetailedViewById(issue.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueStateIsCorrect() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        issue.setCurrentState(null);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, null, null);
        Mockito.verify(issueDAO, Mockito.never()).save(issue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfIssueAssignedStateAndEmptyAssignedUserTrowException() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        User user = new User();
        user.setUuid("1");
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
        User user = new User();
        user.setUuid("1111");
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        Mockito.when(issueDAO.getIssueById(issue.getUuid())).thenReturn(issue);
        issueService.changeIssueState(issue, newState, user);
        Mockito.verify(userDAO).getUserById(user.getUuid());
        Mockito.verify(issueDAO).save(issue);
    }
}