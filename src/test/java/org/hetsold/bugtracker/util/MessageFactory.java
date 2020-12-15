package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MessageFactory {
    private User user;

    public MessageFactory(User user) {
        this.user = user;
    }

    public synchronized Message getMessage(MessageFactoryCreatedMessageType createdMessageType) {
        Message message = new Message();
        message.setTitle("correct title");
        message.setContent("correct content");
        message.setMessageDate(new Date());
        message.setMessageCreator(user);
        switch (createdMessageType) {
            case IncorrectNullMessage:
                message = null;
                break;
            case IncorrectNullContentMessage:
                message.setContent("");
                break;
            case IncorrectNullCreator:
                message.setMessageCreator(null);
                break;
            case IncorrectNullDateMessage:
                message.setMessageDate(null);
            case CorrectMessage:
                break;
            case CorrectMessageNow:
                break;
            case CorrectMessageMinusTwoDays:
                message.setMessageDate(Date.from(LocalDateTime.now().minusDays(2).atZone(ZoneId.systemDefault()).toInstant()));
                break;
            case CorrectMessageMinusFiveDays:
                message.setMessageDate(Date.from(LocalDateTime.now().minusDays(5).atZone(ZoneId.systemDefault()).toInstant()));
                break;
        }
        return message;
    }
}
