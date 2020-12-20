package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class DefaultMessageService implements MessageService {
    private MessageDAO messageDAO;

    @Autowired
    public DefaultMessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    @Override
    public void saveMessage(Message message, User user) {
        if (message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message content can not be empty");
        }
        if (user == null) {
            throw new IllegalArgumentException("user not exists");
        }
        if (getMessageById(message) != null) {
            message.setMessageCreator(user);
            message.setCreateDate(new Date());
        } else {
            message.setMessageEditor(user);
            message.setEditDate(new Date());
        }
        messageDAO.save(message);
    }

    public Message getMessageById(Message message) {
        return messageDAO.getMessageById(message.getUuid());
    }

    @Override
    public void deleteMessage(Message message) {
        messageDAO.delete(message);
    }
}
