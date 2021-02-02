package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;

import java.util.List;

public interface TicketFacade {
    void createTicket(TicketDTO ticketDTO, UserDTO userDTO);

    void deleteTicket(String ticketId);

    void updateTicket(TicketDTO ticketDTO);

    List<TicketDTO> getTicketList();
}
