package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DetailedTicketBean {
    private String uuid;
    private TicketDTO selectedTicketDTO;

    private List<MessageDTO> ticketMessages;

    @Autowired
    private TicketService ticketService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        ticketMessages = new ArrayList<>();
    }

    public void preInitData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            selectedTicketDTO = ticketService.getTicketDTO(uuid);

            ticketMessages = ticketService.getTicketMessages(selectedTicketDTO, 0, 99);
        }

    }

    public TicketDTO getSelectedTicketDTO() {
        return selectedTicketDTO;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void createIssueFromTicket() {

    }

    public List<MessageDTO> getTicketMessages() {
        return ticketMessages;
    }
}
