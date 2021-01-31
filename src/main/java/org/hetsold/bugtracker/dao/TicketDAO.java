package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.model.filter.Contract;

import java.util.List;

public interface TicketDAO {
    void save(Ticket ticket);

    Ticket getTicketById(String uuid);

    void delete(Ticket ticket);

    List<Ticket> getTicketListReportedByUser(User user, Contract contract, int startPosition, int limit);

    long getTicketCountReportedByUser(User user, Contract contract);

    List<Ticket> getTickets(Contract contract, int startPosition, int limit);

    long getTicketsCount(Contract contract);
}
