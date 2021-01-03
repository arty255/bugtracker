package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.User;

public interface MessageService {
    Message saveMessage(Message message, User user);

    void deleteMessage(MessageDTO messageDTO);

    void deleteMessage(Message message);

    Message getMessageById(Message message);
}