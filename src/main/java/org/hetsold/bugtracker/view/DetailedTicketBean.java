package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.Date;

@ManagedBean
@ViewScoped
public class DetailedTicketBean implements Serializable {
    private String uuid;
    private TicketDTO ticket;
    private TicketMessageLazyDataModel messageLazyDataModel;
    private MessageDTO selectedToEditMessage;
    private MessageDTO selectedToDeleteMessage;
    private IssueShortDTO createdIssue;
    private boolean editMode;

    private UserDTO activeUser;

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
        initMessageAction();
        /*todo: getUser FromSecurityContext*/
        activeUser = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            ticket = ticketService.getTicketDTO(uuid);
            initMessages();
        }
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

    public void addMessageToTicketAction() {
        ticketService.addTicketMessage(ticket, selectedToEditMessage, activeUser);
        initMessageAction();
    }

    public void editTicketMessageAction() {
        messageService.saveOrUpdateMessage(selectedToEditMessage, activeUser);
        initMessageAction();
        editMode = false;
    }

    public void deleteMessageAction() {
        messageService.deleteMessage(selectedToDeleteMessage);
        if (selectedToDeleteMessage == selectedToEditMessage) {
            cancelEditAction();
        }
    }

    public void initMessages() {
        messageLazyDataModel = new TicketMessageLazyDataModel(ticketService, messageService, ticket);
    }

    public void initMessageAction() {
        selectedToEditMessage = new MessageDTO("");
    }

    public void editMessagePrepareAction() {
        editMode = true;
    }

    public void cancelEditAction() {
        editMode = false;
        initMessageAction();
    }

    public void initPreviewListener() {
        if (editMode) {
            selectedToEditMessage.setEditor(activeUser);
            selectedToEditMessage.setEditDate(new Date());
        } else {
            selectedToEditMessage.setCreator(activeUser);
            selectedToEditMessage.setCreateDate(new Date());
        }
    }

    public void messageDateAscendingInListListener(){
        messageLazyDataModel.setInverseDateOrder(false);
    }

    public void messageDateDescendingInListListener(){
        messageLazyDataModel.setInverseDateOrder(true);
    }

    public TicketDTO getTicket() {
        return ticket;
    }

    public TicketMessageLazyDataModel getMessageLazyDataModel() {
        return messageLazyDataModel;
    }

    public MessageDTO getSelectedToEditMessage() {
        return selectedToEditMessage;
    }

    public void setSelectedToEditMessage(MessageDTO selectedToEditMessage) {
        this.selectedToEditMessage = selectedToEditMessage;
    }

    public MessageDTO getSelectedToDeleteMessage() {
        return selectedToDeleteMessage;
    }

    public void setSelectedToDeleteMessage(MessageDTO selectedToDeleteMessage) {
        this.selectedToDeleteMessage = selectedToDeleteMessage;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}