package org.hetsold.bugtracker.service;


import org.hetsold.bugtracker.model.Ticket;

public interface TicketService {

    void save(Ticket ticket);
}
