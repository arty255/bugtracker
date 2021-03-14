package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.FactoryIssueType;
import org.hetsold.bugtracker.util.IssueFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.List;

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

    private static final User savedUser = new User("user1", "user1");
    private static final Issue savedIssue = new Issue("description", "steps", savedUser);
    private IssueFactory issueFactory;

    @BeforeClass
    public static void prepareData() {

    }

    @Before
    public void prepareUserData() {
        userDAO.save(savedUser);
        issueDAO.save(savedIssue);
        issueFactory = new IssueFactory(savedIssue, savedUser);
    }

    @Test
    public void checkIfIssueCanBeSaved() {
        Issue issue = issueFactory.getIssue(FactoryIssueType.ISSUE_WITH_PERSISTED_USER);
        issueDAO.save(issue);
        List<Issue> issues = issueDAO.getIssueList(null, 0, 100);
        assertEquals("incorrect issues count after save", 2, issues.size());
    }

    @Test
    public void checkIfIssueCanBeDeleted() {
        issueDAO.delete(savedIssue);
        List<Issue> issues = issueDAO.getIssueList(null, 0, 100);
        assertEquals("incorrect issues count after delete", 0, issues.size());
    }

    @Test
    public void checkIfIssueCanBeFoundById() {
        Issue resultIssue = issueDAO.getIssueByUUID(savedIssue.getUuid());
        assertNotNull(resultIssue);
    }
}