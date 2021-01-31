package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface MessageService {
    Message saveNewMessage(Message message, User user);

    Message updateMessage(Message message, User user);

    MessageDTO saveOrUpdateMessage(MessageDTO messageDTO, UserDTO userDTO);

    Message saveOrUpdateMessage(Message message, User user);

    void delete(MessageDTO messageDTO);

    void delete(Message message);

    Message getMessageById(String uuid);

    MessageDTO getMessageDTOById(String uuid);

    long getMessageCountByTicket(Ticket ticket);

    List<Message> getMessageListByTicket(Ticket ticket, int fromIndex, int limit, boolean inverseDateOrder);
}