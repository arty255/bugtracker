package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.AppConfig;
import org.hetsold.bugtracker.TestAppConfig;
import org.hetsold.bugtracker.dao.TicketDAO;
import org.hetsold.bugtracker.dao.UserDAO;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;
import org.hetsold.bugtracker.model.User;
import org.hetsold.bugtracker.util.TicketFactory;
import org.hetsold.bugtracker.util.TicketFactoryTicketType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, TestAppConfig.class})
@ActiveProfiles(profiles = {"test", "mock"})
public class DefaultTicketServiceTest {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TicketDAO ticketDao;

    private final User user = new User("First Name", "Last Name");
    private final TicketFactory ticketFactory = new TicketFactory(user);

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNoDescriptionCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketEmptyDescription);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.never()).save(ticket);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithEmptyUserCantBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.IncorrectTicketNullCreator);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.never()).save(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkIfTicketWithNotExistedUserCantBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(userDAO.getUserById(ticket.getCreatedBy().getUuid())).thenReturn(null);
        ticketService.save(ticket);
        Mockito.verify(ticketDao, Mockito.times(1)).getTicketById(ticket.getUuid());
        Mockito.verify(ticketDao, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void checkIfCorrectTicketCanBeSaved() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        Mockito.when(userDAO.getUserById(ticket.getCreatedBy().getUuid())).thenReturn(ticket.getCreatedBy());
        ticketService.save(ticket);
        Mockito.verify(ticketDao).save(ticket);
    }

    @Test
    public void checkIfTicketCanBeDeleted() {
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ticketService.delete(ticket);
        Mockito.verify(ticketDao).delete(ticket);
    }

    @Test
    public void checkIfTicketApplyForIssue() {
        ArgumentCaptor<Ticket> ticketArgumentCaptor = ArgumentCaptor.forClass(Ticket.class);
        Ticket ticket = ticketFactory.getTicket(TicketFactoryTicketType.CorrectTicket);
        ticketService.applyForIssue(ticket);
        Mockito.verify(ticketDao, Mockito.atLeastOnce()).save(ticketArgumentCaptor.capture());
        Ticket transformedTicket = ticketArgumentCaptor.getValue();
        assertEquals(TicketResolveState.Resolving, transformedTicket.getResolveState());
        assertEquals(TicketVerificationState.Verified, transformedTicket.getVerificationState());
    }
}