package org.hetsold.bugtracker.service;


import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface TicketService {

    void save(Ticket ticket, User user);

    void addNewTicket(TicketDTO ticketDTO, UserDTO userDTO);

    List<Ticket> getTickets();

    List<TicketDTO> getTicketDtoList();

    Ticket getTicketById(String uuid);

    TicketDTO getTicketDTO(String uuid);

    void delete(Ticket ticket);

    void delete(String uuid);

    void applyForIssue(Ticket ticket);

    void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO, UserDTO userDTO);

    void addTicketMessage(Ticket ticket, Message message, User user);

    List<MessageDTO> getTicketMessages(TicketDTO ticket, int fromIndex, int size);
}
