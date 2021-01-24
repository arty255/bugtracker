package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.UserService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

@ManagedBean
@ViewScoped
public class DetailedIssueBean implements Serializable {
    private String uuid;
    private IssueDTO issue;
    @Autowired
    private IssueService issueService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    private LazyDataModel<IssueEventDTO> historyEventDataModel;

    private boolean isOriginalStateChanged;
    private IssueState originalIssueState;

    private LazyDataModel<UserDTO> userDTODataModel;
    private UserDTO selectedToAssignUser;

    private MessageDTO selectedToEditMessage;
    private MessageDTO selectedToDeleteMessage;
    private boolean editMode;

    private UserDTO activeUser;

    @PostConstruct
    public void initBean() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        isOriginalStateChanged = false;
        selectedToAssignUser = null;
        userDTODataModel = new UserDTOLazyDataModel(userService);
        activeUser = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
        initMessageListener();
    }

    public void preInitIssue() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            issue = issueService.getIssueDTOById(uuid);
            originalIssueState = issue.getCurrentIssueState();
            initHistory();
            initMessageListener();
        }
    }

    public void initHistory() {
        historyEventDataModel = new HistoryEventLazyDataModel(issueService, messageService, issue);
    }

    public void issueStateChanged() {
        isOriginalStateChanged = issue.getCurrentIssueState() != originalIssueState;
    }

    public void changeIssueState() {
        try {
            issueService.changeIssueState(issue, issue.getCurrentIssueState(), activeUser);
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "issue state can not be changed", "issue state can not be changed to " + issue.getCurrentIssueState().getLabel()));
        }
        issue = issueService.getIssueDTOById(uuid);
    }

    public void unAssignUser() {
        issueService.changeIssueAssignedUser(issue, null, activeUser);
    }

    public void reAssignUser() {
        issueService.changeIssueAssignedUser(issue, selectedToAssignUser, activeUser);
    }

    public void initMessageListener() {
        selectedToEditMessage = new MessageDTO();
    }

    public void editMessageAction() {
        messageService.saveOrUpdateMessage(selectedToEditMessage, activeUser);
        initMessageListener();
        editMode = false;
    }

    public void saveMessageAction() {
        issueService.addIssueMessage(issue, selectedToEditMessage, activeUser);
        initMessageListener();
        editMode = false;

    }

    public void deleteMessageAction() {
        issueService.deleteIssueMessage(selectedToDeleteMessage);
        if (selectedToEditMessage == selectedToDeleteMessage) {
            cancelEditAction();
        }
    }

    public void editMessagePrepareAction() {
        editMode = true;
    }

    public void cancelEditAction() {
        editMode = false;
        initMessageListener();
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

    public void save() {
        issue = issueService.saveOrUpdateIssue(issue, activeUser);
    }

    public IssueDTO getIssue() {
        return issue;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isOriginalStateChanged() {
        return isOriginalStateChanged;
    }

    public UserDTO getSelectedToAssignUser() {
        return selectedToAssignUser;
    }

    public void setSelectedToAssignUser(UserDTO selectedToAssignUser) {
        this.selectedToAssignUser = selectedToAssignUser;
    }

    public LazyDataModel<UserDTO> getUserDTODataModel() {
        return userDTODataModel;
    }

    public MessageDTO getSelectedToEditMessage() {
        return selectedToEditMessage;
    }

    public void setSelectedToEditMessage(MessageDTO selectedToEditMessage) {
        this.selectedToEditMessage = selectedToEditMessage;
    }

    public LazyDataModel<IssueEventDTO> getHistoryEventDataModel() {
        return historyEventDataModel;
    }

    public MessageDTO getSelectedToDeleteMessage() {
        return selectedToDeleteMessage;
    }

    public void setSelectedToDeleteMessage(MessageDTO selectedToDeleteMessage) {
        this.selectedToDeleteMessage = selectedToDeleteMessage;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }


}