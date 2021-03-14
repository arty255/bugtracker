package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.hetsold.bugtracker.view.ListableMessageBean;
import org.hetsold.bugtracker.view.issue.IssuesLazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class DetailedTicketBean extends ListableMessageBean implements Serializable {
    private static final long serialVersionUID = 5927752950619086401L;
    private String uuid;
    private TicketDTO ticket;
    private TicketMessageLazyDataModel messageLazyDataModel;
    private IssuesLazyDataModel issuesLazyDataModel;
    private IssueShortDTO createdIssue;
    private IssueShortDTO selectedIssue;

    @Autowired
    private transient TicketService ticketService;
    @Autowired
    private transient MessageService messageService;
    @Autowired
    private transient IssueService issueService;

    @PostConstruct
    public void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initMessageListener();
        activeUser = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
    }

    public void initData() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            initTicket();
            issuesLazyDataModel = new IssuesLazyDataModel(issueService);
            initMessages();
        }
    }

    private void initTicket() {
        ticket = ticketService.getTicketDTO(new TicketDTO(uuid));
    }

    public void initMessages() {
        messageLazyDataModel = new TicketMessageLazyDataModel(ticketService, messageService, ticket);
    }

    public void linkIssueAction() {
        issueService.linkIssueToTicket(selectedIssue, ticket, false);
        initTicket();
        selectedIssue = null;
    }

    public void unlinkIssueAction() {
        issueService.unLinkIssue(selectedIssue);
        initTicket();
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

    public void tickedChangedListener() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!", "Ticked changed. Dont forget save."));
    }

    @Override
    public void preformUpdateOperation(MessageDTO messageDTO) {
        messageService.updateMessage(messageDTO, activeUser);
    }

    @Override
    public void preformSaveOperation(MessageDTO messageDTO) {
        messageDTO.setCreator(activeUser);
        ticketService.addTicketMessage(ticket, messageDTO);
    }

    @Override
    public void preformDeleteOperation(MessageDTO messageDTO) {
        messageService.delete(messageDTO);
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

    public IssuesLazyDataModel getIssuesLazyDataModel() {
        return issuesLazyDataModel;
    }

    public IssueShortDTO getSelectedIssue() {
        return selectedIssue;
    }

    public void setSelectedIssue(IssueShortDTO selectedIssue) {
        this.selectedIssue = selectedIssue;
    }
}