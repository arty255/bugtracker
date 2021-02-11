package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

@ManagedBean
@ViewScoped
public class AddTicketBean implements Serializable {
    private UserDTO activeUser;
    private TicketDTO ticket;
    @Autowired
    private TicketService ticketService;


    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        activeUser = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
        initTicket();
    }

    private void initTicket() {
        ticket = new TicketDTO("");
        ticket.setVerificationState(TicketVerificationState.NotVerified);
        ticket.setResolveState(TicketResolveState.NotResolved);
        ticket.setProductVersion("");
        ticket.setDescription("");
        ticket.setReproduceSteps("");
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    public void addTicket() {
        ticket.setCreationTime(new Date());
        ticket.setUser(activeUser);
        ticketService.addTicket(ticket);
        clearTicket();
    }

    public void clearTicket() {
        initTicket();
    }
}
