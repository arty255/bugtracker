package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.util.IssueFactoryCreatedIssueType;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class HistoryEventHibernateDAOTest {
    @Autowired
    private HistoryEventDAO historyEventDAO;
    @Autowired
    private IssueDAO issueDAO;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDAO userDAO;

    private static Issue issue;
    private static User user;
    private static Message message;

    @BeforeClass
    public static void prepareData() {
        issue = new Issue();
        user = new User("user_1", "user_1");
        message = new Message(user, "message content");
    }

    @Before
    public void preSaveData() {
        issueDAO.save(issue);
        userDAO.save(user);
        messageDAO.save(message);
    }

    @Test
    public void checkIfIssueMessageCanBeSaved() {
        HistoryIssueMessageEvent messageEvent = new HistoryIssueMessageEvent();
        messageEvent.setIssue(issue);
        messageEvent.setMessage(message);
        messageEvent.setEventDate(new Date());
        historyEventDAO.saveIssueMessage(messageEvent);
        HistoryIssueMessageEvent resultMessageEvent = historyEventDAO.getMessageEventById(messageEvent.getUuid());
        assertEquals(resultMessageEvent.getUuid(), messageEvent.getUuid());
    }

    @Test
    public void checkIfIssueStateChangeEventCanBeSaved() {
        HistoryIssueStateChangeEvent stateChangeEvent = new HistoryIssueStateChangeEvent();
        stateChangeEvent.setEventDate(new Date());
        stateChangeEvent.setExpectedFixVersion("1.0.0.1");
        historyEventDAO.saveStateChange(stateChangeEvent);
        HistoryIssueStateChangeEvent resultStateChangeEvent = historyEventDAO.getStateChangeEventById(stateChangeEvent.getUuid());
        assertEquals(resultStateChangeEvent.getUuid(), stateChangeEvent.getUuid());
        assertEquals(resultStateChangeEvent.getState(), stateChangeEvent.getState());
        assertEquals(resultStateChangeEvent.getExpectedFixVersion(), stateChangeEvent.getExpectedFixVersion());
    }
}
