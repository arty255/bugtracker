package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.exception.EmptyMessageContentException;
import org.hetsold.bugtracker.util.MessageFactory;
import org.junit.After;
import org.junit.Before;
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
public class MessageServiceImplTest {
    @Mock
    private MessageDAO messageDAO;
    @Mock
    private UserServiceInternal userService;
    @InjectMocks
    private final MessageServiceImpl messageService = new MessageServiceImpl();

    private User savedUser;
    private Message savedMessage;
    private MessageFactory messageFactory;

    @Before
    public void beforeTest() {
        savedUser = new User.Builder().withNames("first name", "last name").build();
        messageFactory = new MessageFactory(savedUser, savedUser);
        savedMessage = messageFactory.getMessage(MessageFactory.MessageType.NEW_MESSAGE);
        MockitoAnnotations.openMocks(this);
        Mockito.when(messageDAO.getMessageById(savedMessage.getUuid())).thenReturn(savedMessage);
        Mockito.when(userService.getUser(savedUser)).thenReturn(savedUser);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_nullMessageThrowException() {
        messageService.saveNewMessage((Message) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_nullMessageContentThrowException() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.EMPTY_CONTENT_MESSAGE);
        messageService.saveNewMessage(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_nullCreatorThrowException() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NULL_CREATOR_MESSAGE);
        messageService.saveNewMessage(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_notPersistedCreatorThrowException() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NOT_PERSISTED_CREATOR);
        messageService.saveNewMessage(message);
    }

    @Test
    public void saveNewMessage_canBeSave() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NEW_MESSAGE);
        message.setUuid(null);
        message.setContent("new message content");
        messageService.saveNewMessage(message);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).save(messageCaptor.capture());
        assertEquals("new message content", messageCaptor.getValue().getContent());
    }

    @Test(expected = EmptyMessageContentException.class)
    public void updateMessage_emptyMessageContentThrowException() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.EMPTY_CONTENT_MESSAGE);
        messageService.updateMessage(message, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMessage_emptyMessageEditorThrowException() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NEW_MESSAGE);
        message.setUuid(savedMessage.getUuid());
        messageService.updateMessage(message, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateMessage_notPersistedMessageEditorThrowException() {
        User messageEditor = new User.Builder().withNames("test", "test").build();
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NEW_MESSAGE);
        message.setUuid(savedUser.getUuid());
        message.setMessageEditor(messageEditor);
        messageService.updateMessage(message, messageEditor);
    }

    @Test
    public void updateMessage_correctUpdate() {
        Message message = messageFactory.getMessage(MessageFactory.MessageType.NEW_MESSAGE);
        message.setUuid(savedMessage.getUuid());
        message.setContent("unique content");
        messageService.updateMessage(message, savedUser);
        assertEquals("unique content", message.getContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMessage_nullMessageThrowException() {
        messageService.getMessage((Message) null);
    }

    @Test()
    public void getMessage_correctGet() {
        Message messageResult = messageService.getMessage(savedMessage);
        assertEquals(savedMessage.getUuid(), messageResult.getUuid());
    }

    @Test
    public void delete_correctDelete() {
        ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
        messageService.delete(savedMessage);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).delete(messageArgumentCaptor.capture());
        assertEquals(savedMessage.getUuid(), messageArgumentCaptor.getValue().getUuid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void delete_notPersistedMessageThrowException() {
        Message message = new Message.Builder()
                .withCreator(savedUser)
                .withContent("content")
                .build();
        messageService.delete(message);
    }


    @Test(expected = IllegalArgumentException.class)
    public void delete_nullMessageThrowException() {
        messageService.delete((Message) null);
    }
}
