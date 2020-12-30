package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SimplyTicketFacade implements TicketFacade {
    private TicketService ticketService;
    private Ticket ticket;

    @Autowired
    public SimplyTicketFacade(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void createTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        ticket = TicketConvertor.getTicket(ticketDTO);
        ticket.setCreatedBy(UserConvertor.getUser(userDTO));
        ticketService.save(ticket, UserConvertor.getUser(userDTO));
    }

    @Override
    public void deleteTicket(String ticketId) {
        ticketService.delete(TicketConvertor.getTicket(new TicketDTO(ticketId)));
    }

    @Override
    public void updateTicket(TicketDTO ticketDTO) {
        //ticketService.update(ticketConvertor.getTicket(ticketDTO));
    }

    @Override
    public List<TicketDTO> getTicketList() {
        return ticketService.getTickets().stream().map(TicketConvertor::getTicketDTO).collect(Collectors.toList());
    }
}