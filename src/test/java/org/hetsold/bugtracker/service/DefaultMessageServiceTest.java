package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.MessageFactory;
import org.hetsold.bugtracker.util.MessageFactoryCreatedMessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAppConfig.class, AppConfig.class})
@ActiveProfiles(profiles = {"test"})
public class DefaultMessageServiceTest {
    @InjectMocks
    private final MessageService messageService = new DefaultMessageService();
    @Mock
    private MessageDAO messageDAO;
    @Mock
    private UserService userService;

    private static User user = new User("first name", "last name");
    private static MessageFactory messageFactory;

    @BeforeClass
    public static void prepareData() {
        messageFactory = new MessageFactory(user);
    }

    @Before
    public void beforeTest() {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfEmptyMessageThrowException() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        message.setContent("");
        messageService.saveNewMessage(message, user);
    }

    @Test
    public void checkIfMessageCanBeSaved() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        message.setUuid("");
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(null);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        messageService.saveNewMessage(message, user);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).save(messageCaptor.capture());
        Message capturedMessage = messageCaptor.getValue();
        assertEquals(capturedMessage.getMessageCreator(), user);
    }

    @Test
    public void checkIfMessageCanBeUpdated() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(message);
        Mockito.when(userService.getUserById(user.getUuid())).thenReturn(user);
        String newMessageContent = "new Content";
        message.setContent(newMessageContent);
        messageService.saveOrUpdateMessage(message, user);
        assertEquals(newMessageContent, message.getContent());
    }

    @Test
    public void checkIfMessageCanBeDeleted() {
        Message message = messageFactory.getMessage(MessageFactoryCreatedMessageType.CorrectMessage);
        Mockito.when(messageDAO.getMessageById(message.getUuid())).thenReturn(message);
        messageService.deleteMessage(message);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).delete(message);
    }
}
