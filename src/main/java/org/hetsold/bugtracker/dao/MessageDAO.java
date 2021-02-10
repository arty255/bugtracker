package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Ticket;

import java.util.List;
import java.util.UUID;

public interface MessageDAO {
    void save(Message message);

    Message getMessageById(UUID uuid);

    void delete(Message message);

    List<Message> loadAll();

    long getMessageCountByTicket(Ticket ticket);

    List<Message> getMessageListByTicket(Ticket ticket, int fromIndex, int limit, boolean inverseDateOrder);
}