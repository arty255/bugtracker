package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.IssueFactory;
import org.hetsold.bugtracker.util.IssueFactoryCreatedIssueType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, AppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
@Transactional
public class IssueHibernateDAOTest {
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private UserDAO userDAO;

    private static List<Issue> issueList = new ArrayList<>();
    private static User user = new User("user1", "user1");
    private static IssueFactory issueFactory = new IssueFactory(user);

    @BeforeClass
    public static void prepareData() {
        for (int i = 0; i < 3; i++) {
            issueList.add(issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectDayAgoIssue));
            issueList.add(issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectWeekAgoIssue));
            issueList.add(issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectTwoWeekAgoIssue));
        }
        for (int i = 0; i < 3; i++) {
            issueList.add(issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectMountAgoIssue));
        }
    }

    @Before
    public void prepareUserData() {
        userDAO.save(user);
    }

    @Test
    public void checkIfIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueDAO.save(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 1);
    }

    @Test
    public void checkIfIssueCanBeDeleted() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueDAO.save(issue);
        issueDAO.delete(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 0);
    }

    @Test
    public void checkIfIssueCanBeFoundById() {
        Issue issue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        issueDAO.save(issue);
        Issue resultIssue = issueDAO.getIssueById(issue.getUuid());
        assertNotNull(resultIssue);
        assertEquals(resultIssue.getUuid(), issue.getUuid());
        assertEquals(resultIssue.getIssueNumber(), issue.getIssueNumber());
        assertEquals(resultIssue.getDescription(), issue.getDescription());
    }

    @Test
    public void checkIssueCorrectCount() {
        issueList.forEach(issue -> issueDAO.save(issue));
        assertEquals(issueDAO.getIssueCount(), issueList.size());
    }

    @Test
    public void checkIssueAgeCorrectCount() {
        Date filterDate = Date.from(LocalDateTime.now().minusWeeks(3).atZone(ZoneId.systemDefault()).toInstant());
        List<Issue> resultList = issueList.stream().filter(issue -> issue.getCreationTime().before(filterDate)).collect(Collectors.toList());
        issueList.forEach(issue -> issueDAO.save(issue));
        Issue criteriaIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        criteriaIssue.setCreationTime(filterDate);
        assertEquals(resultList.size(), issueDAO.getIssueByCriteria(criteriaIssue).size());
    }

    @Test
    public void checkIfLoadedIssueHasCorrectScope() {
        Issue testIssue = issueFactory.getIssue(IssueFactoryCreatedIssueType.CorrectIssue);
        testIssue.setReportedBy(userDAO.listAll().get(0));
        issueDAO.save(testIssue);
        Issue resultIssue = issueDAO.getIssueToDetailedViewById(testIssue.getUuid());
        assertEquals(resultIssue.getReportedBy(), testIssue.getReportedBy());
        assertEquals(resultIssue.getReportedBy().getMessageList().size(), 0);
        assertEquals(resultIssue.getReportedBy().getReportedIssues().size(), 0);
    }
}