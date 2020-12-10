package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, org.hetsold.bugtracker.TestAppConfig.class})
@ActiveProfiles("test")
@Transactional
public class MessageHibernateDAOTest {
    @Autowired
    private MessageDAO messageDao;

    @Test
    public void checkIfMessageSaved() {
        User user = new User("test_user1", "test_user1");
        Message message = new Message(user, "message title", "message content");
        messageDao.save(message);
        Message resultMessage = messageDao.getMessageById(message.getUuid());
        assertEquals(message.getUuid(), resultMessage.getUuid());
        assertEquals(message.getTitle(), message.getTitle());
        assertEquals(message.getContent(), message.getContent());
    }
}
