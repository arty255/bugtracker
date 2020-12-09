package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class IssueHibernateDAOTest {
    @Autowired
    private IssueDAO issueDAO;

    private static List<Issue> issueList = new ArrayList<>();

    @BeforeClass
    public static void prepareData() {
        for (int i = 1; i < 4; i++) {
            issueList.add(new Issue.Builder()
                    .withIssueId("test_id" + i)
                    .withIssueAppearanceTime(new Date())
                    .withTicketCreationTime(new Date())
                    .withFullDescription("test full description N" + i)
                    .withShortDescription("no short")
                    .withProductVersion("v 0.1")
                    .withReportedBy(null)
                    .withReproduceSteps("1.start 2.stop").build());
        }
    }

    @Test
    public void checkIfIssueCanBeSaved() {
        issueDAO.save(issueList.get(0));
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 1);
    }

    @Test
    public void checkIfIssueCanBeDeleted() {
        Issue issue = issueList.get(1);
        issueDAO.save(issue);
        issueDAO.delete(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 0);
    }

    @Test
    public void checkIfIssueExistsById() {
        Issue issue = issueList.get(2);
        issueDAO.save(issue);
        Issue resultIssue = issueDAO.getIssueById(issue.getUuid());
        assertNotNull(resultIssue);
        assertEquals(resultIssue.getUuid(), issue.getUuid());
        assertEquals(resultIssue.getIssueId(), issue.getIssueId());
    }

    @Test
    public void checkIssueCorrectCount() {
        issueDAO.save(issueList.get(0));
        issueDAO.save(issueList.get(1));
        issueDAO.save(issueList.get(2));
        assertEquals(issueDAO.getIssueCount(), 3);
    }
}