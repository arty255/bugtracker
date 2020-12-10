package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Message;

public interface MessageDAO {
    void save(Message message);
    Message getMessageById(String uuid);
}
