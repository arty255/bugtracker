package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.facade.MessageConvertor;
import org.hetsold.bugtracker.facade.UserConvertor;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public Message saveNewMessage(Message message, User user) {
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user is null or deleted");
        }
        Message newMessage = new Message();
        newMessage.setContent(message.getContent());
        newMessage.setMessageCreator(user);
        messageDAO.save(newMessage);
        return newMessage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Message updateMessage(Message message, User user) {
        Message oldMessage;
        if (message == null || (oldMessage = messageDAO.getMessageById(message.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect message: message is null or deleted");
        }
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user is null or deleted");
        }
        oldMessage.update(message, user);
        return message;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Message saveOrUpdateMessage(Message message, User user) {
        if (message == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        if (user == null || user.getUuid().isEmpty()) {
            throw new IllegalArgumentException("invalid user: user cannot be empty");
        }
        if (message.getUuid() == null) {
            message = saveNewMessage(message, user);
        } else {
            message = updateMessage(message, user);
        }
        return message;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Message saveOrUpdateMessage(MessageDTO messageDTO, UserDTO userDTO) {
        if (messageDTO == null || messageDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        if (userDTO == null || userDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("invalid user: user cannot be empty");
        }
        return saveOrUpdateMessage(MessageConvertor.getMessage(messageDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Message getMessageById(Message message) {
        if (message == null || message.getUuid() == null || message.getUuid().isEmpty()) {
            return null;
        }
        return messageDAO.getMessageById(message.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Message getMessageById(String uuid) {
        if (uuid.isEmpty()) {
            return null;
        }
        return messageDAO.getMessageById(uuid);
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