package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;

public interface TicketDAO {
    void save(Ticket ticket);

    Ticket getTicketById(String uuid);
}
