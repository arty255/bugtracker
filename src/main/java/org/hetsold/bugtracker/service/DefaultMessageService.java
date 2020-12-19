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
    public void addMessage(Message message, User user) {
        if (message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message content can not be empty");
        }
        if (user == null) {
            throw new IllegalArgumentException("user not exists");
        }
        message.setMessageCreator(user);
        message.setMessageDate(new Date());
        messageDAO.save(message);
    }

    @Override
    public void deleteMessage(Message message) {
        messageDAO.delete(message);
    }
}
