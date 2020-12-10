package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
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
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class MessageHibernateDAOTest {
    @Autowired
    private MessageDAO messageDao;
    @Autowired
    private UserDAO userDAO;
    private static User user;

    @BeforeClass
    public static void prepareData() {
        user = new User("test_user1", "test_user1");
    }

    @Before
    public void preSaveData() {
        userDAO.save(user);
    }

    @Test
    public void checkIfMessageCanBeSaved() {
        Message message = new Message(user, "message title", "message content");
        messageDao.save(message);
        Message resultMessage = messageDao.getMessageById(message.getUuid());
        assertEquals(message.getUuid(), resultMessage.getUuid());
        assertEquals(message.getTitle(), message.getTitle());
        assertEquals(message.getContent(), message.getContent());
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        Message message = new Message(user, "message title", "message content");
        messageDao.save(message);
        messageDao.delete(message);
        List<Message> messageList = messageDao.loadAll();
        assertEquals(messageList.size(), 0);
    }
}
