package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.TestAppConfig;
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
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
@Transactional
public class IssueEventHibernateDAOTest {
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
        IssueMessageEvent messageEvent = new IssueMessageEvent();
        messageEvent.setIssue(issue);
        messageEvent.setMessage(message);
        messageEvent.setEventDate(new Date());
        historyEventDAO.saveIssueMessage(messageEvent);
        IssueMessageEvent resultMessageEvent = historyEventDAO.getMessageEventById(messageEvent.getUuid());
        assertEquals(resultMessageEvent.getUuid(), messageEvent.getUuid());
    }

    @Test
    public void checkIfIssueStateChangeEventCanBeSaved() {
        IssueStateChangeEvent stateChangeEvent = new IssueStateChangeEvent();
        stateChangeEvent.setEventDate(new Date());
        historyEventDAO.saveStateChange(stateChangeEvent);
        IssueStateChangeEvent resultStateChangeEvent = historyEventDAO.getStateChangeEventById(stateChangeEvent.getUuid());
        assertEquals(resultStateChangeEvent.getUuid(), stateChangeEvent.getUuid());
        assertEquals(resultStateChangeEvent.getState(), stateChangeEvent.getState());
    }
}
