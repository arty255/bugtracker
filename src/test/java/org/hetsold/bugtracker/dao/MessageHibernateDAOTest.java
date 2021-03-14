package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactory.MessageType;
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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
@Transactional
public class MessageHibernateDAOTest {
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDAO userDAO;
    private static User user;
    private static MessageFactory messageFactory;

    @BeforeClass
    public static void prepareData() {
        user = new User.Builder().withNames("test_user1", "test_user1").build();
        messageFactory = new MessageFactory(user, user);
    }

    @Before
    public void preSaveData() {
        userDAO.save(user);
    }

    @Test
    public void checkIfMessageCanBeSaved() {
        Message message = messageFactory.getMessage(MessageType.NEW_MESSAGE);
        messageDAO.save(message);
        Message resultMessage = messageDAO.getMessageById(message.getUuid());
        assertEquals(message.getUuid(), resultMessage.getUuid());
        assertEquals(message.getContent(), message.getContent());
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        Message message = messageFactory.getMessage(MessageType.NEW_MESSAGE);
        messageDAO.save(message);
        messageDAO.delete(message);
        List<Message> messageList = messageDAO.loadAll();
        assertEquals("incorrect size", 0, messageList.size());
    }
}