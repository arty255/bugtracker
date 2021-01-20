package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
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
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class DetailedTicketBean implements Serializable {
    private String uuid;
    private TicketDTO ticket;
    private List<MessageDTO> ticketMessages;
    private MessageDTO selectedMessage;
    private IssueShortDTO createdIssue;
    private boolean editMode;

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
        ticketMessages = new ArrayList<>();
        initMessage();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            ticket = ticketService.getTicketDTO(uuid);
            initMessageList();
        }
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void createIssueFromTicket() {
        /*todo: getUser FromSecurityContext*/
        UserDTO user = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        try {
            createdIssue = issueService.createIssueFromTicket(ticket, user);
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No issue created", "Probably issue already exists"));
        }
    }

    public void updateTicket() {
        /*todo: getUser FromSecurityContext*/
        UserDTO user = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        ticketService.updateTicket(ticket, user);
    }

    public void tickedChangedListener(ValueChangeEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Ticked changed. Dont forget save."));
    }

    public void addMessageToTicket() {
        /*todo: getUser FromSecurityContext*/
        UserDTO user = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        ticketService.addTicketMessage(ticket, selectedMessage, user);
        initMessage();
        initMessageList();
    }

    public void editTicketMessage() {
        UserDTO user = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        messageService.saveOrUpdateMessage(selectedMessage, user);
        initMessage();
        initMessageList();
        editMode = false;
    }

    public void deleteMessage(MessageDTO message) {
        messageService.deleteMessage(message);
        initMessageList();
        if(message.equals(selectedMessage)){
            initMessage();
        }
    }


    public void initMessageList() {
        ticketMessages = ticketService.getTicketMessages(ticket, 0, 99);
    }

    public void initMessage() {
        selectedMessage = new MessageDTO("");
    }

    public List<MessageDTO> getTicketMessages() {
        return ticketMessages;
    }

    public MessageDTO getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(MessageDTO selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public IssueShortDTO getCreatedIssue() {
        return createdIssue;
    }

    public void onCloseCreatedIssueDialog() {
        createdIssue = null;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}