package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.mapper.MessageMapper;
import org.hetsold.bugtracker.service.mapper.TicketMapper;
import org.hetsold.bugtracker.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.hetsold.bugtracker.service.ValidationHelper.*;

@Service
public class TicketServiceImpl implements TicketService, TicketServiceInternal {
    private TicketDAO ticketDao;
    private UserServiceInternal userService;
    private MessageServiceInternal messageService;

    @Autowired
    public TicketServiceImpl(TicketDAO ticketDao, UserServiceInternal userService, MessageServiceInternal messageService) {
        this.ticketDao = ticketDao;
        this.userService = userService;
        this.messageService = messageService;
    }

    public TicketServiceImpl() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicket(Ticket ticket) {
        validateTicketBeforeSave(ticket);
        presetResolveAndVerificationState(ticket);
        presetUser(ticket);
        ticket.setUuid(UUID.randomUUID());
        ticketDao.save(ticket);
    }

    private void presetResolveAndVerificationState(Ticket ticket) {
        if (ticket.getResolveState() == null) {
            ticket.setResolveState(TicketResolveState.NotResolved);
        }
        if (ticket.getVerificationState() == null) {
            ticket.setVerificationState(TicketVerificationState.NotVerified);
        }
    }

    private void presetUser(Ticket ticket) {
        User user = userService.getUser(ticket.getCreatedBy());
        validateNotNull(user, "user not persisted");
        ticket.setCreatedBy(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TicketDTO addTicket(TicketDTO ticketDTO) {
        Ticket ticket = TicketMapper.getTicket(ticketDTO);
        addTicket(ticket);
        return TicketMapper.getTicketDTO(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Ticket updateTicket(Ticket ticket, User editor) {
        validateTicketBeforeUpdate(ticket);
        Ticket oldTicket = ticketDao.getTicketById(ticket.getUuid());
        validateNotNull(oldTicket, "ticket is not persisted");
        oldTicket.update(ticket);
        return oldTicket;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TicketDTO updateTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        Ticket ticket = updateTicket(TicketMapper.getTicket(ticketDTO), UserMapper.getUser(userDTO));
        return TicketMapper.getTicketDTO(ticket);
    }

    //@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Ticket> getTickets(Contract contract, int startPosition, int limit) {
        return ticketDao.getTickets(contract, startPosition, limit);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TicketDTO> getTicketList(Contract contract, int startPosition, int limit) {
        return TicketMapper.getTicketDTOList(getTickets(contract, startPosition, limit));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getTicketsCount(Contract contract) {
        return ticketDao.getTicketsCount(contract);
    }

    //@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Ticket> getTicketListReportedByUser(User user, Contract contract, int startPosition, int limit) {
        validateUserAndUUID(user);
        user = userService.getUser(user);
        return ticketDao.getTicketListReportedByUser(user, contract, startPosition, limit);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TicketDTO> getTicketDTOListReportedByUser(UserDTO userDTO, Contract contract, int startPosition, int limit) {
        return TicketMapper.getTicketDTOList(getTicketListReportedByUser(UserMapper.getUser(userDTO), contract, startPosition, limit));
    }

    //@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getTicketCountReportedByUser(User user, Contract contract) {
        validateUserAndUUID(user);
        return ticketDao.getTicketsCountReportedByUser(user, contract);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getTicketCountReportedByUser(UserDTO userDTO, Contract contract) {
        return ticketDao.getTicketsCountReportedByUser(UserMapper.getUser(userDTO), contract);
    }

    @Override
    @Secured("ROLE_DELETE_TICKET")
    public void delete(TicketDTO ticketDTO) {
        delete(TicketMapper.getTicket(ticketDTO));
    }

    @Override
    @Secured("ROLE_DELETE_TICKET")
    public void delete(Ticket ticket) {
        validateTicketAndUUID(ticket);
        ticket = ticketDao.getTicketById(ticket.getUuid());
        ticketDao.delete(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Ticket getTicket(Ticket ticket) {
        validateTicketAndUUID(ticket);
        return ticketDao.getTicketById(ticket.getUuid());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TicketDTO getTicketDTO(TicketDTO ticketDTO) {
        return TicketMapper.getTicketDTO(TicketMapper.getTicket(ticketDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyForIssue(Ticket ticket) {
        validateTicketAndUUID(ticket);
        ticket = ticketDao.getTicketById(ticket.getUuid());
        validateNotNull(ticket, "ticket is not persisted");
        ticket.setVerificationState(TicketVerificationState.Verified);
        ticket.setResolveState(TicketResolveState.Resolving);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyForIssue(TicketDTO ticketDTO) {
        applyForIssue(TicketMapper.getTicket(ticketDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO) {
        addTicketMessage(TicketMapper.getTicket(ticketDTO),
                MessageMapper.getMessage(messageDTO));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(Ticket ticket, Message message) {
        validateTicketAndUUID(ticket);
        ticket = getTicket(ticket);
        validateNotNull(ticket, "ticket is not persisted");
        messageService.saveNewMessage(message);
        if (!ticket.getMessageList().contains(message)) {
            ticket.getMessageList().add(message);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<MessageDTO> getTicketMessages(TicketDTO ticketDTO, int fromIndex, int limit, boolean inverseDateOrder) {
        Ticket ticket = TicketMapper.getTicket(ticketDTO);
        validateTicketAndUUID(ticket);
        ticket = ticketDao.getTicketById(ticket.getUuid());
        return MessageMapper.getMessageDTOList(messageService.getMessagesForTicket(ticket, fromIndex, limit, inverseDateOrder));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getMessagesCountByTicket(TicketDTO ticketDTO) {
        return getMessagesCountByTicket(TicketMapper.getTicket(ticketDTO));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getMessagesCountByTicket(Ticket ticket) {
        validateTicketAndUUID(ticket);
        return messageService.getMessagesCountForTicket(ticket);
    }
}