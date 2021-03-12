package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.MessageDAO;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.service.mapper.MessageMapper;
import org.hetsold.bugtracker.service.mapper.TicketMapper;
import org.hetsold.bugtracker.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.hetsold.bugtracker.service.ValidationHelper.*;

@Service
public class MessageServiceImpl implements MessageService, MessageServiceInternal {
    private MessageDAO messageDAO;
    private UserServiceInternal userService;

    @Autowired
    public MessageServiceImpl(UserServiceInternal userService, MessageDAO messageDAO) {
        this.userService = userService;
        this.messageDAO = messageDAO;
    }

    public MessageServiceImpl() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNewMessage(Message message) {
        validateMessageBeforeSave(message);
        message.setUuid(UUID.randomUUID());
        messageDAO.save(message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveNewMessage(MessageDTO messageDTO) {
        saveNewMessage(MessageMapper.getMessage(messageDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Message updateMessage(Message message, User user) {
        validateMessageContent(message);
        Message oldMessage = messageDAO.getMessageById(message.getUuid());
        validateNotNull(message, "message can not be not persisted");
        User fetchedUser = userService.getUser(user);
        validateNotNull(fetchedUser, "user can not be not persisted");
        oldMessage.updateContentAndEditor(message, fetchedUser);
        return oldMessage;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateMessage(MessageDTO messageDTO, UserDTO messageEditorDto) {
        updateMessage(MessageMapper.getMessage(messageDTO), UserMapper.getUser(messageEditorDto));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Message getMessage(Message message) {
        validateNotNullEntityAndUUID(message);
        return messageDAO.getMessageById(message.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MessageDTO getMessage(MessageDTO messageDTO) {
        return MessageMapper.getMessageDTO(getMessage(MessageMapper.getMessage(messageDTO)));
    }

    @Override
    public long getMessagesCountForTicket(Ticket ticket) {
        validateNotNullEntityAndUUID(ticket);
        return messageDAO.getMessageCountByTicket(ticket);
    }

    @Override
    public List<Message> getMessagesForTicket(Ticket ticket, int startPosition, int limit, boolean inverseDateOrder) {
        validateNotNullEntityAndUUID(ticket);
        return messageDAO.getMessageListByTicket(ticket, startPosition, limit, inverseDateOrder);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<MessageDTO> getMessagesForTicket(TicketDTO ticketDTO, int fromIndex, int limit, boolean inverseDateOrder) {
        Ticket ticket = TicketMapper.getTicket(ticketDTO);
        return MessageMapper.getMessageDTOList(getMessagesForTicket(ticket, fromIndex, limit, inverseDateOrder));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getMessagesCountForTicket(TicketDTO ticketDTO) {
        return getMessagesCountForTicket(TicketMapper.getTicket(ticketDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(MessageDTO messageDTO) {
        delete(MessageMapper.getMessage(messageDTO));
    }

    @Override
    public void delete(Message message) {
        validateNotNullEntityAndUUID(message);
        Message fetchedMessage = getMessage(message);
        validateNotNull(fetchedMessage, "message not persisted");
        messageDAO.delete(fetchedMessage);
    }
}