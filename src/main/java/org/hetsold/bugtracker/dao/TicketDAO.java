package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

import java.util.List;
import java.util.UUID;

public interface TicketDAO {
    void save(Ticket ticket);

    Ticket getTicketById(UUID uuid);

    void delete(Ticket ticket);

    List<Ticket> getTicketListReportedByUser(User user, Contract contract, int startPosition, int limit);

    long getTicketsCountReportedByUser(User user, Contract contract);

    List<Ticket> getTickets(Contract contract, int startPosition, int limit);

    long getTicketsCount(Contract contract);
}
