package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;

import java.util.List;

public interface TicketFacade {
    void createTicket(TicketDTO ticketDTO, UserDTO userDTO);

    void deleteTicket(String ticketId);

    void updateTicket(TicketDTO ticketDTO);

    List<TicketDTO> getTicketList();
}
