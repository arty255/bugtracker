package org.hetsold.bugtracker.service;


import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.model.filter.Contract;

import java.util.List;

public interface TicketService {

    TicketDTO addTicket(TicketDTO ticketDTO, UserDTO userDTO);

    TicketDTO updateTicket(TicketDTO ticketDTO);

    List<Ticket> getTickets(Contract contract, int startPosition, int limit);

    List<TicketDTO> getTicketDTOList(Contract contract, int startPosition, int limit);

    long getTicketsCount(Contract contract);

    List<TicketDTO> getTicketDTOListReportedByUser(UserDTO userDTO, Contract contract, int startPosition, int limit);

    long getTicketCountReportedByUser(UserDTO userDTO, Contract contract);

    Ticket getTicketById(String uuid);

    TicketDTO getTicketDTOById(String uuid);

    void delete(TicketDTO ticketDTO);

    void delete(String uuid);

    void applyForIssue(Ticket ticket);

    void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO, UserDTO userDTO);

    void addTicketMessage(Ticket ticket, Message message, User user);

    List<MessageDTO> getTicketMessages(TicketDTO ticket, int fromIndex, int limit, boolean inverseDateOrder);

    long getMessagesCountByTicket(TicketDTO ticketDTO);
}
