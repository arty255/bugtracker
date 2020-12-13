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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class IssueServiceTest {
    private Issue correctIssue;
    private Issue incorrectIssueUser;
    private Issue incorrectIssueDate;
    private Issue incorrectIssueDescription;
    private User user;

    @Autowired
    private IssueService issueService;
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private UserDAO userDAO;

    @Before
    public void prepareData() {
        user = new User("reportUser", "report user");
        correctIssue = new Issue.Builder()
                .withIssueId("issue number 1")
                .withIssueAppearanceTime(new Date())
                .withTicketCreationTime(new Date())
                .withProductVersion("product version v0.1")
                .withShortDescription("short description")
                .withFullDescription("full description")
                .withReproduceSteps("1.step 1, 2.step 2")
                .withReportedBy(user)
                .build();
        incorrectIssueUser = new Issue.Builder()
                .withIssueId("issue number 1")
                .withIssueAppearanceTime(new Date())
                .withTicketCreationTime(new Date())
                .withProductVersion("product version v0.1")
                .withShortDescription("short description")
                .withFullDescription("full description")
                .withReproduceSteps("1.step 1, 2.step 2")
                .withReportedBy(null)
                .build();
        incorrectIssueDate = new Issue.Builder()
                .withIssueId("issue number 1")
                .withIssueAppearanceTime(Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant()))
                .withTicketCreationTime(Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant()))
                .withProductVersion("product version v0.1")
                .withShortDescription("short description")
                .withFullDescription("full description")
                .withReproduceSteps("1.step 1, 2.step 2")
                .withReportedBy(user)
                .build();
        incorrectIssueDescription = new Issue.Builder()
                .withIssueId("issue number 1")
                .withIssueAppearanceTime(new Date())
                .withTicketCreationTime(new Date())
                .withProductVersion("product version v0.1")
                .withShortDescription("")
                .withFullDescription("")
                .withReproduceSteps("1.step 1, 2.step 2")
                .withReportedBy(user)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueDatesSaveThrowException() {
        issueService.save(incorrectIssueDate);
        Mockito.verify(issueDAO, Mockito.never()).save(incorrectIssueDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIncorrectIssueUserSaveThrowException() {
        issueService.save(incorrectIssueUser);
        Mockito.verify(issueDAO, Mockito.never()).save(incorrectIssueUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDescriptionAndShortDescriptionSaveThrowException() {
        issueService.save(incorrectIssueDescription);
        Mockito.verify(issueDAO, Mockito.never()).save(incorrectIssueDescription);
    }

    @Test
    public void checkIfCorrectIssueCanBeSaved() {
        Mockito.when(userDAO.getUserById(correctIssue.getReportedBy().getUuid())).thenReturn(correctIssue.getReportedBy());
        issueService.save(correctIssue);
        Mockito.verify(issueDAO).save(correctIssue);
    }

    @Test
    public void checkIfIssuePresentInListById() {
        Mockito.when(issueDAO.getIssueById(correctIssue.getUuid())).thenReturn(correctIssue);
        Issue resultIssue = issueService.getIssueById(correctIssue.getUuid());
        Mockito.verify(issueDAO).getIssueById(correctIssue.getUuid());
        assertEquals(correctIssue.getUuid(), resultIssue.getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkEmptyIssueDeleteThrowException() {
        issueService.deleteIssue(null);
        Mockito.verify(userDAO, Mockito.never()).delete(null);
    }

    @Test
    public void checkIfIssueCanBeeDeleted() {
        issueService.deleteIssue(correctIssue);
        Mockito.verify(issueDAO).delete(correctIssue);
    }
}