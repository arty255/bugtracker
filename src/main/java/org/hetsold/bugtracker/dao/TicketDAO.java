package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;

import java.util.List;

public interface TicketDAO {
    void save(Ticket ticket);

    Ticket getTicketById(String uuid);

    List<Ticket> loadAll();

    void delete(Ticket ticket);
}
