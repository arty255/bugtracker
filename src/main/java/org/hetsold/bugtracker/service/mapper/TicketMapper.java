package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class TicketMapper {
    public static Ticket getTicket(TicketDTO ticketDTO) {
        if (ticketDTO != null) {
            Ticket ticket = new Ticket();
            ticket.setUuid(UUIDMapper.getUUID(ticketDTO));
            mapDataField(ticketDTO, ticket);
            return ticket;
        }
        return null;
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
        if (ticketDTO.getUser() != null) {
            ticket.setCreatedBy(UserMapper.getUser(ticketDTO.getUser()));
        }
    }

    public static TicketDTO getTicketDTO(Ticket ticket) {
        return new TicketDTO(ticket);
    }

    public static List<TicketDTO> getTicketDTOList(List<Ticket> tickets) {
        return tickets.stream().map(TicketDTO::new).collect(Collectors.toList());
    }
}