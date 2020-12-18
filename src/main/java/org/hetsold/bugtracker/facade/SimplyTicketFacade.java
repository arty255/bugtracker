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
    private TicketConvertor ticketConvertor;
    private UserConvertor userConvertor;
    private Ticket ticket;

    @Autowired
    public SimplyTicketFacade(TicketService ticketService, TicketConvertor ticketConvertor, UserConvertor userConvertor) {
        this.ticketService = ticketService;
        this.ticketConvertor = ticketConvertor;
        this.userConvertor = userConvertor;
    }

    @Override
    public void createTicket(TicketDTO ticketDTO, UserDTO userDTO) {
        ticket = ticketConvertor.getTicket(ticketDTO);
        ticket.setCreatedBy(userConvertor.getUser(userDTO));
        ticketService.save(ticket);
    }

    @Override
    public void deleteTicket(String ticketId) {
        ticketService.delete(ticketConvertor.getTicket(new TicketDTO(ticketId)));
    }

    @Override
    public void updateTicket(TicketDTO ticketDTO) {
        //ticketService.update(ticketConvertor.getTicket(ticketDTO));
    }

    @Override
    public List<TicketDTO> getTicketList() {
        return ticketService.getTickets().stream().map(ticketConvertor::getTicketDTO).collect(Collectors.toList());
    }
}