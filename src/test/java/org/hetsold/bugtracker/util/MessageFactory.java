package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class MessageFactory {
    private final User user;

    public MessageFactory(User user) {
        this.user = user;
    }

    public synchronized Message getMessage(MessageFactoryCreatedMessageType createdMessageType) {
        Message message = new Message();
        message.setContent("correct content");
        message.setCreateDate(new Date());
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
                message.setCreateDate(null);
            case CorrectMessage:
                break;
            case CorrectMessageNow:
                break;
            case CorrectMessageMinusTwoDays:
                message.setCreateDate(Date.from(LocalDateTime.now().minusDays(2).atZone(ZoneId.systemDefault()).toInstant()));
                break;
            case CorrectMessageMinusFiveDays:
                message.setCreateDate(Date.from(LocalDateTime.now().minusDays(5).atZone(ZoneId.systemDefault()).toInstant()));
                break;
        }
        return message;
    }
}
