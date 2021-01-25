package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.facade.MessageConvertor;
import org.hetsold.bugtracker.facade.TicketConvertor;
import org.hetsold.bugtracker.facade.UserConvertor;
import org.hetsold.bugtracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Ticket ticket, User user) {
        if (ticket.getDescription().isEmpty()) {
            throw new IllegalArgumentException("description can not be empty");
        }
        if (user == null || (user = userService.getUserById(user.getUuid())) == null) {
            throw new IllegalArgumentException("user in ticket can not be empty");
        }
        ticket.setCreationTime(new Date());
        ticket.setCreatedBy(user);
        ticketDao.save(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        save(produceNewTicketFromDto(ticketDTO), userService.getUserById(userDTO.getUuid()));
    }

    private Ticket produceNewTicketFromDto(TicketDTO ticketDTO) {
        Ticket ticket = TicketConvertor.getTicket(ticketDTO);
        if (ticket.getUuid() == null || ticket.getUuid().isEmpty()) {
            ticket.setUuid(java.util.UUID.randomUUID().toString());
        }
        if (ticket.getResolveState() == null) {
            ticket.setResolveState(TicketResolveState.NotResolved);
        }
        if (ticket.getVerificationState() == null) {
            ticket.setVerificationState(TicketVerificationState.NotVerified);
        }
        return ticket;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ticket> getTickets() {
        return ticketDao.loadAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketDtoList() {
        return getTickets().stream().map(TicketConvertor::getTicketDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> getTicketDtoListReportedByUser(UserDTO userDTO, int startPosition, int limit) {
        User user;
        if (userDTO == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be not or unpersisted");
        }
        return ticketDao.getTicketListReportedByUser(user, startPosition, limit)
                .stream()
                .map(TicketConvertor::getTicketDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTicketCountReportedByUser(UserDTO userDTO) {
        User user;
        if (userDTO == null || userDTO.getUuid().isEmpty() || (user = userService.getUserById(userDTO.getUuid())) == null) {
            throw new IllegalArgumentException("incorrect user: user can not be not or unpersisted");
        }
        return ticketDao.getTicketCountReportedByUser(user);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketDao.delete(ticket);
    }

    @Override
    public void delete(String uuid) {
        ticketDao.delete(ticketDao.getTicketById(uuid));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Ticket getTicketById(String uuid) {
        return ticketDao.getTicketById(uuid);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TicketDTO getTicketDTO(String uuid) {
        return TicketConvertor.getTicketDTO(getTicketById(uuid));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void applyForIssue(Ticket ticket) {
        if (ticket == null || (ticket = getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        ticket.setVerificationState(TicketVerificationState.Verified);
        ticket.setResolveState(TicketResolveState.Resolving);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO, UserDTO userDTO) {
        addTicketMessage(TicketConvertor.getTicket(ticketDTO), MessageConvertor.getMessage(messageDTO), UserConvertor.getUser(userDTO));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addTicketMessage(Ticket ticket, Message message, User user) {
        if (ticket == null || (ticket = ticketDao.getTicketById(ticket.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        message = messageService.saveOrUpdateMessage(message, user);
        if (!ticket.getMessageList().contains(message)) {
            ticket.getMessageList().add(message);
        }
    }

    @Override
    public List<MessageDTO> getTicketMessages(TicketDTO ticketDTO, int fromIndex, int limit, boolean inverseDateOrder) {
        Ticket ticket;
        if (ticketDTO == null || (ticket = ticketDao.getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        return messageService.getMessageListByTicket(ticket, fromIndex, limit, inverseDateOrder)
                .stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public long getMessagesCountByTicket(TicketDTO ticketDTO) {
        Ticket ticket;
        if (ticketDTO == null || (ticket = ticketDao.getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        return messageService.getMessageCountByTicket(ticket);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTicket(TicketDTO ticketDTO, UserDTO user) {
        Ticket ticket;
        if (ticketDTO == null || (ticket = ticketDao.getTicketById(ticketDTO.getUuid())) == null) {
            throw new IllegalArgumentException("ticket can not be empty");
        }
        ticket.setVerificationState(ticketDTO.getVerificationState());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setProductVersion(ticketDTO.getProductVersion());
        ticket.setReproduceSteps(ticketDTO.getReproduceSteps());
        ticket.setResolveState(ticketDTO.getResolveState());
    }
}