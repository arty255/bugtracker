package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.TicketFactory;
import org.hetsold.bugtracker.util.TicketFactoryTicketType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class TicketServiceTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDao;

    private final User user = new User("firstName", "lastName");
    private final TicketFactory ticketFactory = new TicketFactory(user);

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNoDescriptionCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketEmptyDescription);
        ticketService.save(ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithEmptyUserCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketNullCreator);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.never()).save(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNotExistedUserCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(userDAO.getUserById(ticket.getCreatedBy().getUuid())).thenReturn(null);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.never()).save(Mockito.any());
    }
}