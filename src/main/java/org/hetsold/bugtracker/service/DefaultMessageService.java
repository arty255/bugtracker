package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.facade.MessageMapper;
import org.hetsold.bugtracker.facade.UserMapper;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (user == null || user.getUuid() == null || user.getUuid().isEmpty() || (user = userService.getUserById(user.getUuid())) == null) {
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
        if (user == null || user.getUuid() == null || user.getUuid().isEmpty() || (user = userService.getUserById(user.getUuid())) == null) {
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
    public MessageDTO saveOrUpdateMessage(MessageDTO messageDTO, UserDTO userDTO) {
        if (messageDTO == null || messageDTO.getContent() == null || messageDTO.getContent().isEmpty()) {
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        if (userDTO == null || userDTO.getUuid() == null || userDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("invalid user: user cannot be empty");
        }
        return new MessageDTO(saveOrUpdateMessage(MessageMapper.getMessage(messageDTO), UserMapper.getUser(userDTO)));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Message getMessageById(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        return messageDAO.getMessageById(uuid);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MessageDTO getMessageDTOById(String uuid) {
        return MessageMapper.getMessageDTO(getMessageById(uuid));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getMessageCountByTicket(Ticket ticket) {
        return messageDAO.getMessageCountByTicket(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Message> getMessageListByTicket(Ticket ticket, int fromIndex, int limit, boolean inverseDateOrder) {
        return messageDAO.getMessageListByTicket(ticket, fromIndex, limit, inverseDateOrder);
    }

    @Override
    public void delete(MessageDTO messageDTO) {
        if(messageDTO == null){
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        delete(MessageMapper.getMessage(messageDTO));
    }

    @Override
    public void delete(Message message) {
        if (message == null || message.getUuid() == null || message.getUuid().isEmpty() || (message = getMessageById(message.getUuid())) == null) {
            throw new IllegalArgumentException("invalid message: message or message content can not be empty");
        }
        messageDAO.delete(message);
    }
}