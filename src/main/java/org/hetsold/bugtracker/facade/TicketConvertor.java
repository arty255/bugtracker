package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketConvertor {
    private TicketDAO ticketDAO;

    @Autowired
    public TicketConvertor(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    public Ticket getTicket(TicketDTO ticketDTO) {
        if (ticketDTO != null && !ticketDTO.getUuid().isEmpty()) {
            return ticketDAO.getTicketById(ticketDTO.getUuid());
        }
        return null;
    }

    public TicketDTO getTicketDTO(Ticket ticket) {
        return new TicketDTO(ticket);
    }
}