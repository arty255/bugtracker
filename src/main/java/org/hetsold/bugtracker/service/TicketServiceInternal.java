package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

public interface TicketServiceInternal {

    void addTicket(Ticket ticket);

    Ticket updateTicket(Ticket ticket, User editor);

    Ticket getTicket(Ticket ticket);

    void delete(Ticket ticket);

    void applyForIssue(Ticket ticket);

    void addTicketMessage(Ticket ticket, Message message);

    long getMessagesCountByTicket(Ticket ticketDTO);
}