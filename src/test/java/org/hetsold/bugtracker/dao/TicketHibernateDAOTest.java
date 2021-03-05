package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.TicketFactory;
import org.hetsold.bugtracker.util.TicketFactoryTicketType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {org.hetsold.bugtracker.AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", ""})
@Transactional
public class TicketHibernateDAOTest {
    @Autowired
    private TicketDAO ticketDao;
    private final User user = new User("first name", "last name");
    private final TicketFactory ticketFactory = new TicketFactory(user);

    @Test
    public void checkIfTicketCanBeSavedAndFound() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ticketDao.save(ticket);
        Ticket resultTicket = ticketDao.getTicketById(ticket.getUuid());
        assertEquals(resultTicket.getUuid(), ticket.getUuid());
        assertEquals(resultTicket.getDescription(), ticket.getDescription());
        assertEquals(resultTicket.getCreationTime(), resultTicket.getCreationTime());
    }
}