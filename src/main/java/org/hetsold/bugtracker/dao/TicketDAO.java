package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface TicketDAO {
    void save(Ticket ticket);

    Ticket getTicketById(String uuid);

    List<Ticket> loadAll();

    void delete(Ticket ticket);

    List<Ticket> getTicketListReportedByUser(User user, int startPosition, int limit);

    long getTicketCountReportedByUser(User user);
}
