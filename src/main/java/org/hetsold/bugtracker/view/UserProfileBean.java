package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class UserProfileBean implements Serializable {
    private String uuid;
    private UserDTO user;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;

    private LazyDataModel<TicketDTO> reportedTickets;
    private int totalReportedTickets;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);

    }

    public void initUserData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initUserProfileData();
        }
    }

    public void initUserProfileData() {
        user = userService.getUserDTOById(uuid);
        this.totalReportedTickets = (int) ticketService.getTicketCountReportedByUser(user);
        reportedTickets = new TicketLazyDataModel(ticketService, user);
    }

    public void save() {
        userService.updateUser(user);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LazyDataModel<TicketDTO> getReportedTickets() {
        return reportedTickets;
    }

    public int getTotalReportedTickets() {
        return totalReportedTickets;
    }
}