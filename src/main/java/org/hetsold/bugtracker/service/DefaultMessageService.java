package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.facade.MessageConvertor;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class DefaultMessageService implements MessageService {
    private MessageDAO messageDAO;
    private UserService userService;

    @Autowired
    public DefaultMessageService(UserService userService, MessageDAO messageDAO) {
        this.userService = userService;
        this.messageDAO = messageDAO;
    }

    public DefaultMessageService() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Message saveMessage(Message message, User user) {
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("message content can not be empty");
        }
        String newMessageContent = message.getContent();
        message = getMessageById(message);
        if ((user = userService.getUserById(user)) == null) {
            throw new IllegalArgumentException("user not exists");
        }
        if (message == null) {
            message = new Message();
            message.setContent(newMessageContent);
            message.setMessageCreator(user);
            message.setCreateDate(new Date());
            messageDAO.save(message);
        } else {
            message.setContent(newMessageContent);
            message.setMessageEditor(user);
            message.setEditDate(new Date());
        }
        return message;
    }

    @Transactional(readOnly = true)
    public Message getMessageById(Message message) {
        if (message == null || message.getUuid() == null || message.getUuid().isEmpty()) {
            return null;
        }
        return messageDAO.getMessageById(message.getUuid());
    }

    @Override
    public void deleteMessage(MessageDTO messageDTO) {
        deleteMessage(MessageConvertor.getMessage(messageDTO));
    }

    @Override
    public void deleteMessage(Message message) {
        message = getMessageById(message);
        messageDAO.delete(message);
    }
}
