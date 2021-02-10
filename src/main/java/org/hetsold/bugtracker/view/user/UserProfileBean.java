package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.dto.FullUserDetails;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.hetsold.bugtracker.view.ticket.TicketLazyDataModel;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private UserDTO activeUser;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        activeUser = ((FullUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
    }

    public void initUserData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initUserProfileData();
        }
    }

    public void initUserProfileData() {
        user = userService.getUser(new UserDTO(uuid));
        this.totalReportedTickets = (int) ticketService.getTicketCountReportedByUser(user, null);
        reportedTickets = new TicketLazyDataModel(ticketService, user);
    }

    public void save() {
        /*todo:*/
        userService.updateUserProfileData(user);
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

    public UserDTO getActiveUser() {
        return activeUser;
    }
}