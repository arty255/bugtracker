package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;

import java.util.List;

public interface MessageDAO {
    void save(Message message);

    Message getMessageById(String uuid);

    void delete(Message message);

    List<Message> loadAll();
}