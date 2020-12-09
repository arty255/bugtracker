package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class IssueHibernateDAOTest {
    @Autowired
    private IssueDAO issueDAO;

    @Test
    public void checkIfIssueCanBeSaved() {
        Issue issue = new Issue();
        issueDAO.save(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 1);
    }

    @Test
    public void checkIfIssueCanBeDeleted() {
        Issue issue = new Issue();
        issueDAO.save(issue);
        issueDAO.delete(issue);
        List<Issue> issues = issueDAO.listAll();
        assertEquals(issues.size(), 0);
    }

    @Test
    public void checkIssueCorrectCount() {
        issueDAO.save(new Issue());
        issueDAO.save(new Issue());
        issueDAO.save(new Issue());
        assertEquals(issueDAO.getIssueCount(), 3);
    }
}