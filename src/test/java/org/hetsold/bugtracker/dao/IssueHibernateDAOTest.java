package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.IssueFactory;
import org.hetsold.bugtracker.service.IssueType;
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
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class IssueHibernateDAOTest {
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private UserDAO userDAO;

    private static List<Issue> issueList = new ArrayList<>();
    private static User firstUser = new User("user1", "user1");
    private static User secondUser = new User("user2", "user2");
    private static IssueFactory issueFactory = new IssueFactory(firstUser, secondUser);

    @BeforeClass
    public static void prepareData() {
        for (int i = 0; i < 3; i++) {
            issueList.add(issueFactory.getIssue(IssueType.CorrectDayAgoIssue));
            issueList.add(issueFactory.getIssue(IssueType.CorrectWeekAgoIssue));
            issueList.add(issueFactory.getIssue(IssueType.CorrectTwoWeekAgoIssue));
        }
        for (int i = 0; i < 3; i++) {
            issueList.add(issueFactory.getIssue(IssueType.CorrectMountAgoIssue));
        }
    }


    @Test
    public void checkIfIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        userDAO.save(issue.getReportedBy());
        issueDAO.save(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 1);
    }

    @Test
    public void checkIfIssueCanBeDeleted() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        userDAO.save(issue.getReportedBy());
        issueDAO.save(issue);
        issueDAO.delete(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 0);
    }

    @Test
    public void checkIfIssueCanBeFoundById() {
        Issue issue = issueFactory.getIssue(IssueType.CorrectIssue);
        userDAO.save(issue.getReportedBy());
        issueDAO.save(issue);
        Issue resultIssue = issueDAO.getIssueById(issue.getUuid());
        assertNotNull(resultIssue);
        assertEquals(resultIssue.getUuid(), issue.getUuid());
        assertEquals(resultIssue.getIssueId(), issue.getIssueId());
        assertEquals(resultIssue.getFullDescription(), issue.getFullDescription());
    }

    @Test
    public void checkIssueCorrectCount() {
        issueList.forEach(issue -> {
            issueDAO.save(issue);
            userDAO.save(issue.getReportedBy());
        });
        assertEquals(issueDAO.getIssueCount(), issueList.size());
    }

    @Test
    public void checkIssueAgeCorrectCount() {
        userDAO.save(firstUser);
        userDAO.save(secondUser);
        Date filterDate = Date.from(LocalDateTime.now().minusWeeks(3).atZone(ZoneId.systemDefault()).toInstant());
        List<Issue> resultList = issueList.stream().filter(issue -> issue.getIssueAppearanceTime().before(filterDate)).collect(Collectors.toList());
        issueList.forEach(issue -> issueDAO.save(issue));
        Issue criteriaIssue = issueFactory.getIssue(IssueType.CorrectIssue);
        criteriaIssue.setTicketCreationTime(filterDate);
        assertEquals(resultList.size(), issueDAO.getIssueByCriteria(criteriaIssue).size());
    }
}