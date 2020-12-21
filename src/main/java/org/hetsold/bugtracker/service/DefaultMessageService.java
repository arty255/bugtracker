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

    public DefaultMessageService() {
    }

    @Override
    public void saveMessage(Message message, User user) {
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message content can not be empty");
        }
        String newMessageContent = message.getContent();
        message = getMessageById(message);
        if (user == null) {
            throw new IllegalArgumentException("user not exists");
        }
        if (message == null) {
            message = new Message();
            message.setMessageCreator(user);
            message.setCreateDate(new Date());
            messageDAO.save(message);
        } else {
            message.setContent(newMessageContent);
            message.setMessageEditor(user);
            message.setEditDate(new Date());
        }
    }

    public Message getMessageById(Message message) {
        return messageDAO.getMessageById(message.getUuid());
    }

    @Override
    public void deleteMessage(Message message) {
        messageDAO.delete(message);
    }
}
