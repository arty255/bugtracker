package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;

public interface MessageService {
    void addMessage(Message message, User user);

    void deleteMessage(Message message);
}
