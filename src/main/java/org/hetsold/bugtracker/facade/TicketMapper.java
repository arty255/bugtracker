package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {
    public static Ticket getTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        if (ticketDTO != null) {
            ticket = new Ticket();
            ticket.setUuid(ticketDTO.getUuid());
            mapDataField(ticketDTO, ticket);
        }
        return ticket;
    }

    public static Ticket getTicketWithAutogeneratedId(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        mapDataField(ticketDTO, ticket);
        return ticket;
    }

    private static void mapDataField(TicketDTO ticketDTO, Ticket ticket) {
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setReproduceSteps(ticketDTO.getReproduceSteps());
        ticket.setProductVersion(ticketDTO.getProductVersion());
        ticket.setCreationTime(ticketDTO.getCreationTime());
        ticket.setVerificationState(ticketDTO.getVerificationState());
        ticket.setResolveState(ticketDTO.getResolveState());
    }

    public static TicketDTO getTicketDTO(Ticket ticket) {
        return new TicketDTO(ticket);
    }

    public static List<TicketDTO> getTicketDTOList(List<Ticket> tickets) {
        return tickets.stream().map(TicketDTO::new).collect(Collectors.toList());
    }
}