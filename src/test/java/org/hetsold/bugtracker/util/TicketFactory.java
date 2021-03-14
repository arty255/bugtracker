package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

import java.util.Date;

public class TicketFactory {
    private final User user;

    public TicketFactory(User user) {
        this.user = user;
    }

    public Ticket getTicket(TicketFactoryTicketType ticketFactoryTicketType) {
        Ticket ticket = new Ticket.Builder().build();
        ticket.setCreationTime(new Date());
        ticket.setDescription("description");
        ticket.setCreatedBy(user);
        ticket.setProductVersion("product v1");
        switch (ticketFactoryTicketType) {
            case IncorrectTicketEmptyDescription:
                ticket.setDescription("");
                break;
            case IncorrectTicketNullCreator:
                ticket.setCreatedBy(null);
                break;
            case IncorrectTicketWrongCreationTime:
                ticket.setCreationTime(null);
                break;
        }
        return ticket;
    }
}
