package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;

public interface MessageService {
    Message saveNewMessage(Message message, User user);

    Message updateMessage(Message message, User user);

    Message saveOrUpdateMessage(MessageDTO messageDTO, UserDTO userDTO);

    Message saveOrUpdateMessage(Message message, User user);

    void deleteMessage(MessageDTO messageDTO);

    void deleteMessage(Message message);

    Message getMessageById(Message message);

    Message getMessageById(String uuid);
}