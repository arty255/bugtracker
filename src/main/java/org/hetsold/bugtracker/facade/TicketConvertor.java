package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketDTO;
import org.springframework.stereotype.Component;

@Component
public class TicketConvertor {

    public Ticket getTicket(TicketDTO ticketDTO) {
        Ticket ticket = null;
        if (ticketDTO != null) {
            ticket = new Ticket();
            ticket.setUuid(ticketDTO.getUuid());
            ticket.setDescription(ticketDTO.getDescription());
            ticket.setReproduceSteps(ticketDTO.getReproduceSteps());
            ticket.setProductVersion(ticketDTO.getProductVersion());
            ticket.setCreationTime(ticketDTO.getCreationTime());
            ticket.setVerificationState(ticketDTO.getVerificationState());
            ticket.setResolveState(ticketDTO.getResolveState());
        }
        return ticket;
    }

    public TicketDTO getTicketDTO(Ticket ticket) {
        return new TicketDTO(ticket);
    }
}