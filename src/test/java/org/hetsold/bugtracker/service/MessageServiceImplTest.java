package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.exception.EmptyMessageContentException;
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

    private static final User savedUser = new User("first name", "last name");
    private static final Message savedMessage = new Message(savedUser, "messageContent");

    @Before
    public void beforeTest() {
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
        Message message = new Message(savedUser, "");
        messageService.saveNewMessage(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_nullCreatorThrowException() {
        Message message = new Message((User) null, "message content");
        messageService.saveNewMessage(message);
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNewMessage_notPersistedCreatorThrowException() {
        User notPersistedUser = new User("test", "test");
        Message message = new Message(notPersistedUser, "message content");
        messageService.saveNewMessage(message);
    }

    @Test
    public void saveNewMessage_canBeSave() {
        Message message = new Message(null, savedUser, "new message content");
        messageService.saveNewMessage(message);
        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        Mockito.verify(messageDAO, Mockito.atLeastOnce()).save(messageCaptor.capture());
        assertEquals("new message content", messageCaptor.getValue().getContent());
    }

    @Test(expected = EmptyMessageContentException.class)
    public void updateMessage_emptyMessageContentThrowException() {
        Message message = new Message(savedUser.getUuid(), "");
        messageService.updateMessage(message, savedUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateMessage_emptyMessageEditorThrowException() {
        Message message = new Message(savedUser.getUuid(), savedUser, "ne content");
        messageService.updateMessage(message, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateMessage_notPersistedMessageEditorThrowException() {
        User messageEditor = new User("test", "test");
        Message message = new Message(savedUser.getUuid(), savedUser, "ne content");
        messageService.updateMessage(message, messageEditor);
    }

    @Test
    public void updateMessage_correctUpdate() {
        Message message = new Message(savedMessage.getUuid(), savedUser, "newContent_unique");
        messageService.updateMessage(message, savedUser);
        assertEquals("newContent_unique", message.getContent());
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
        Message message = new Message(savedUser, "content");
        messageService.delete(message);
    }


    @Test(expected = IllegalArgumentException.class)
    public void delete_nullMessageThrowException() {
        messageService.delete((Message) null);
    }
}
