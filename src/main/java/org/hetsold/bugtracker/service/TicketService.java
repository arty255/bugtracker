package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;

import java.util.List;

public interface TicketService {
    TicketDTO addTicket(TicketDTO ticketDTO);

    TicketDTO updateTicket(TicketDTO ticketDTO, UserDTO userDTO);

    List<TicketDTO> getTicketList(Contract contract, int startPosition, int limit);

    long getTicketsCount(Contract contract);

    List<TicketDTO> getTicketDTOListReportedByUser(UserDTO userDTO, Contract contract, int startPosition, int limit);

    long getTicketCountReportedByUser(UserDTO userDTO, Contract contract);

    TicketDTO getTicketDTO(TicketDTO ticketDTO);

    void delete(TicketDTO ticketDTO);

    void applyForIssue(TicketDTO ticket);

    void addTicketMessage(TicketDTO ticketDTO, MessageDTO messageDTO);

    List<MessageDTO> getTicketMessages(TicketDTO ticket, int fromIndex, int limit, boolean inverseDateOrder);

    long getMessagesCountByTicket(TicketDTO ticketDTO);
}