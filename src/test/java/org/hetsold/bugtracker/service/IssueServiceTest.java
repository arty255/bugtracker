package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Issue;
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
        Issue issue = new Issue();
        issueService.findIssueByCriteria(issue);
        Mockito.verify(issueDAO).getIssueByCriteria(issue);
    }
}