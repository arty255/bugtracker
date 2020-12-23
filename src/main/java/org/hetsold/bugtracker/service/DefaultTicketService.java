package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DefaultTicketService implements TicketService {
    private TicketDAO ticketDao;
    private UserDAO userDAO;
    private MessageService messageService;

    @Autowired
    public DefaultTicketService(TicketDAO ticketDao, UserDAO userDAO, MessageService messageService) {
        this.ticketDao = ticketDao;
        this.userDAO = userDAO;
        this.messageService = messageService;
    }

    public DefaultTicketService() {

    }

    @Override
    public void save(Ticket ticket) {
        if (ticket.getDescription().isEmpty()) {
            throw new IllegalArgumentException("description can not be empty");
        }
        if (ticket.getCreatedBy() == null || userDAO.getUserById(ticket.getCreatedBy().getUuid()) == null) {
            throw new IllegalArgumentException("user in ticket can not be empty");
        }
        ticket.setCreationTime(new Date());
        ticketDao.save(ticket);
    }

    @Override
    public List<Ticket> getTickets() {
        return ticketDao.loadAll();
    }

    @Override
    public void delete(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    public Ticket getTicketById(String uuid) {
        return ticketDao.getTicketById(uuid);
    }

    @Override
    public void applyForIssue(Ticket ticket) {
        if (ticket == null || (ticket = getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        ticket.setVerificationState(TicketVerificationState.Verified);
        ticket.setResolveState(TicketResolveState.Resolving);
    }

    @Override
    public void addTicketMessage(Ticket ticket, Message message, User user) {
        if (ticket == null || (ticket = ticketDao.getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        messageService.saveMessage(message, user);
        ticket.getMessageList().add(message);
    }
}