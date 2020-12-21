package org.hetsold.bugtracker.service;


import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface TicketService {

    void save(Ticket ticket);

    List<Ticket> getTickets();

    Ticket getTicketById(String uuid);

    void delete(Ticket ticket);

    void applyForIssue(Ticket ticket);

    void addTicketMessage(Ticket ticket, Message message, User user);
}
