package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

@ManagedBean
@ViewScoped
public class AddTicketBean implements Serializable {
    private TicketDTO ticket;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @ManagedProperty("#{ticketListBean}")
    private TicketListBean ticketListBean;


    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
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
        //for now get hardcoded user from db, registered user will be obtained after spring security integration
        UserDTO user = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        ticket.setCreationTime(new Date());
        ticketService.addNewTicket(ticket, user);
        clearTicket();
        ticketListBean.updateTicketsList();
    }

    public void clearTicket() {
        initTicket();
    }

    public void setTicketListBean(TicketListBean ticketListBean) {
        this.ticketListBean = ticketListBean;
    }
}
