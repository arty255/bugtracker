package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, AppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class DefaultMessageServiceTest {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDAO userDAO;
    private static User user = new User("first name", "last name");
    private static MessageFactory messageFactory;

    @BeforeClass
    public static void prepareData() {
        messageFactory = new MessageFactory(user);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyMessageThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        message.setContent("");
        messageService.saveMessage(message, user);
    }

    @Test
    public void checkIfMessageCanBeSaved() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        messageService.saveMessage(message, user);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).save(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        assertEquals(capturedMessage.getMessageCreator(), user);
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(message);
        messageService.deleteMessage(message);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).delete(message);
    }

    @Test
    public void checkIfMessageContentCanBeUpdated() {
        userDAO.save(user);
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        String newContent = "new content";
        message.setContent(newContent);
        Mockito.when(userDAO.getUserById(user.getUuid())).thenReturn(user);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        messageService.saveMessage(message, user);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).save(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        assertEquals(newContent, capturedMessage.getContent());
    }

}
