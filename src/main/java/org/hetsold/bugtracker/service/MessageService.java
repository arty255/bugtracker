package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;

public interface MessageService {
    void saveMessage(Message message, User user);

    void deleteMessage(Message message);

    Message getMessageById(Message message);
}
