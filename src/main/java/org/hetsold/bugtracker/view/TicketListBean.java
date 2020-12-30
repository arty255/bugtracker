package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean
@ViewScoped
public class TicketListBean {
    private List<TicketDTO> ticketDTOList;
    @Autowired
    private TicketService ticketService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        updateTicketsList();
    }

    public void deleteTicket(String uuid) {
        ticketService.delete(uuid);
        updateTicketsList();
    }

    public void updateTicketsList() {
        ticketDTOList = ticketService.getTicketDtoList();
    }

    public List<TicketDTO> getTicketDTOList() {
        return ticketDTOList;
    }
}