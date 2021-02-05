package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.facade.MessageMapper;
import org.hetsold.bugtracker.facade.TicketMapper;
import org.hetsold.bugtracker.facade.UserMapper;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DefaultTicketService implements TicketService {
    private TicketDAO ticketDao;
    private UserService userService;
    private MessageService messageService;

    @Autowired
    public DefaultTicketService(TicketDAO ticketDao, UserService userService, MessageService messageService) {
        this.ticketDao = ticketDao;
        this.userService = userService;
        this.messageService = messageService;
    }

    public DefaultTicketService() {

    }

    private void save(Ticket ticket, User user) {
        if (ticket.getUuid() == null || ticket.getUuid().isEmpty()) {
            throw new IllegalArgumentException("preset operation error: uuid is empty");
        }
        if (ticket.getDescription().isEmpty()) {
            throw new IllegalArgumentException("description can not be empty");
        }
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user in ticket can not be empty");
        }
        ticket.setCreatedBy(user);
        ticketDao.save(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TicketDTO addTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        if (ticketDTO == null || ticketDTO.getDescription() == null || ticketDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("incorrect ticket: ticket description can not be empty");
        }
        User user;
        if (userDTO == null || userDTO.getUuid() == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be empty");
        }
        Ticket ticket = TicketMapper.getTicketWithAutogeneratedId(ticketDTO);
        presetFields(ticket);
        save(ticket, user);
        return TicketMapper.getTicketDTO(ticket);
    }

    private void presetFields(Ticket ticket) {
        if (ticket.getResolveState() == null) {
            ticket.setResolveState(TicketResolveState.NotResolved);
        }
        if (ticket.getVerificationState() == null) {
            ticket.setVerificationState(TicketVerificationState.NotVerified);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public TicketDTO updateTicket(TicketDTO ticketDTO) {
        Ticket oldTicket;
        if (ticketDTO == null || ticketDTO.getUuid() == null || ticketDTO.getUuid().isEmpty() || (oldTicket = ticketDao.getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty or not persisted");
        }
        oldTicket.update(TicketMapper.getTicket(ticketDTO));
        return new TicketDTO(oldTicket);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Ticket> getTickets(Contract contract, int startPosition, int limit) {
        return ticketDao.getTickets(contract, startPosition, limit);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<TicketDTO> getTicketDTOList(Contract contract, int startPosition, int limit) {
        return TicketMapper.getTicketDTOList(getTickets(contract, startPosition, limit));
    }

    @Override
    public long getTicketsCount(Contract contract) {
        return ticketDao.getTicketsCount(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketDTOListReportedByUser(UserDTO userDTO, Contract contract, int startPosition, int limit) {
        User user;
        if (userDTO == null || userDTO.getUuid() == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be not or not persisted");
        }
        return TicketMapper.getTicketDTOList(ticketDao.getTicketListReportedByUser(user, contract, startPosition, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public long getTicketCountReportedByUser(UserDTO userDTO, Contract contract) {
        User user;
        if (userDTO == null || userDTO.getUuid() == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be not or not persisted");
        }
        return ticketDao.getTicketCountReportedByUser(user, contract);
    }

    @Override
    @Secured("ROLE_DELETE_TICKET")
    public void delete(TicketDTO ticketDTO) {
        if (ticketDTO == null || ticketDTO.getUuid() == null || ticketDTO.getUuid().isEmpty()) {
            throw new IllegalArgumentException("incorrect ticket: ticket can not be null or not persisted");
        }
        delete(ticketDTO.getUuid());
    }

    @Override
    @Secured("ROLE_DELETE_TICKET")
    public void delete(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("incorrect ticket uuid: ticket uuid can not be null or empty");
        }
        ticketDao.delete(ticketDao.getTicketById(uuid));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Ticket getTicketById(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            throw new IllegalArgumentException("incorrect ticket uuid: ticket uuid can not be null or empty");
        }
        return ticketDao.getTicketById(uuid);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TicketDTO getTicketDTOById(String uuid) {
        return TicketMapper.getTicketDTO(getTicketById(uuid));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyForIssue(Ticket ticket) {
        if (ticket == null || ticket.getUuid() == null || ticket.getUuid().isEmpty() || (ticket = getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect ticket: ticket can not be empty or not persisted");
        }
        /*todo: implement strategy*/
        ticket.setVerificationState(TicketVerificationState.Verified);
        ticket.setResolveState(TicketResolveState.Resolving);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO, UserDTO userDTO) {
        addTicketMessage(TicketMapper.getTicket(ticketDTO), MessageMapper.getMessage(messageDTO), UserMapper.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(Ticket ticket, Message message, User user) {
        if (ticket == null || ticket.getUuid() == null || ticket.getUuid().isEmpty() || (ticket = getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect ticket: ticket can not be empty or not persisted");
        }
        message = messageService.saveOrUpdateMessage(message, user);
        if (!ticket.getMessageList().contains(message)) {
            ticket.getMessageList().add(message);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<MessageDTO> getTicketMessages(TicketDTO ticketDTO, int fromIndex, int limit, boolean inverseDateOrder) {
        Ticket ticket;
        if (ticketDTO == null || ticketDTO.getUuid() == null || ticketDTO.getUuid().isEmpty() || (ticket = getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect ticket: ticket can not be empty or not persisted");
        }
        return MessageMapper.getMessageDTOList(messageService.getMessageListByTicket(ticket, fromIndex, limit, inverseDateOrder));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public long getMessagesCountByTicket(TicketDTO ticketDTO) {
        Ticket ticket;
        if (ticketDTO == null || ticketDTO.getUuid() == null || ticketDTO.getUuid().isEmpty() || (ticket = getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect ticket: ticket can not be empty or not persisted");
        }
        return messageService.getMessageCountByTicket(ticket);
    }
}