package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class DetailedTicketBean extends ListableMessageBean implements Serializable {
    private String uuid;
    private TicketDTO ticket;
    private TicketMessageLazyDataModel messageLazyDataModel;
    private IssueShortDTO createdIssue;

    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private IssueService issueService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initMessageListener();
        /*todo: getUser FromSecurityContext*/
        activeUser = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            ticket = ticketService.getTicketDTO(uuid);
            initMessages();
        }
    }

    public void initMessages() {
        messageLazyDataModel = new TicketMessageLazyDataModel(ticketService, messageService, ticket);
    }

    public void createIssueFromTicket() {
        try {
            createdIssue = issueService.createIssueFromTicket(ticket, activeUser);
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No issue created", "Probably issue already exists"));
        }
    }

    public void updateTicket() {
        ticketService.updateTicket(ticket, activeUser);
    }

    public void tickedChangedListener(ValueChangeEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Ticked changed. Dont forget save."));
    }

    @Override
    public void preformUpdateOperation(MessageDTO messageDTO) {
        messageService.saveOrUpdateMessage(messageDTO, activeUser);
    }

    @Override
    public void preformSaveOperation(MessageDTO messageDTO) {
        ticketService.addTicketMessage(ticket, messageDTO, activeUser);
    }

    @Override
    public void preformDeleteOperation(MessageDTO messageDTO) {
        messageService.deleteMessage(messageDTO);
    }

    public void messageDateAscendingInListListener() {
        messageLazyDataModel.setInverseDateOrder(false);
    }

    public void messageDateDescendingInListListener() {
        messageLazyDataModel.setInverseDateOrder(true);
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public TicketMessageLazyDataModel getMessageLazyDataModel() {
        return messageLazyDataModel;
    }

    public IssueShortDTO getCreatedIssue() {
        return createdIssue;
    }

    public void onCloseCreatedIssueDialog() {
        createdIssue = null;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}