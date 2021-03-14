package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;

import java.util.Date;

public final class MessageFactory {
    private final User messageCreator;
    private final User messageEditor;

    public MessageFactory(User messageCreator, User messageEditor) {
        this.messageCreator = messageCreator;
        this.messageEditor = messageEditor;
    }

    public synchronized Message getMessage(MessageType createdMessageType) {
        Message message = new Message.Builder()
                .withContent("correct content")
                .withCreator(messageCreator)
                .build();
        message.setCreateDate(new Date());
        switch (createdMessageType) {
            case NULL_MESSAGE:
                message = null;
                break;
            case EMPTY_CONTENT_MESSAGE:
                message.setContent("");
                break;
            case NULL_CREATOR_MESSAGE:
                message.setMessageCreator(null);
                break;
            case NEW_MESSAGE_WITH_EDITOR:
                message.setMessageEditor(messageEditor);
                break;
            case NOT_PERSISTED_CREATOR:
                message.setMessageCreator(new User.Builder().withNames("not persisted", "not persisted").build());
                break;
        }
        return message;
    }

    public enum MessageType {
        NEW_MESSAGE,
        NEW_MESSAGE_WITH_EDITOR,
        NULL_MESSAGE,
        EMPTY_CONTENT_MESSAGE,
        NULL_CREATOR_MESSAGE,
        NOT_PERSISTED_CREATOR
    }
}
