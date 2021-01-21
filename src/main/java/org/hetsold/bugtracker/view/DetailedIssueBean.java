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
import java.util.List;

@ManagedBean
@ViewScoped
public class DetailedIssueBean implements Serializable {
    private String uuid;
    private IssueDTO issue;
    private List<HistoryEvent> issueEvents;
    @Autowired
    private IssueService issueService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    private boolean isOriginalStateChanged;
    private IssueState originalIssueState;

    private LazyDataModel<UserDTO> userDTODataModel;
    private UserDTO selectedToAssignUser;

    private MessageDTO selectedMessage;
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
        issue = issueService.getIssueDTOById(uuid);
        originalIssueState = issue.getCurrentIssueState();
        initEvents();
    }

    public void initEvents() {
        issueEvents = issueService.getIssueEvents(issue, 0, 0);
    }

    public void issueStateChanged() {
        isOriginalStateChanged = issue.getCurrentIssueState() != originalIssueState;
    }

    public void changeIssueState() {
        boolean changeResult = issueService.changeIssueState(issue, issue.getCurrentIssueState(), activeUser);
        issue = issueService.getIssueDTOById(uuid);
        if (changeResult) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "state changed", "state changed to " + issue.getCurrentIssueState().getLabel()));
        }
    }

    public void unAssignUser() {
        issueService.changeIssueAssignedUser(issue, null, activeUser);
    }

    public void reAssignUser() {
        issueService.changeIssueAssignedUser(issue, selectedToAssignUser, activeUser);
    }

    public void initMessageListener() {
        selectedMessage = new MessageDTO();
    }

    public void editMessageListener() {

    }

    public void saveMessageListener() {

    }

    public void save() {
        issue = issueService.saveOrUpdateIssue(issue);
    }

    public IssueDTO getIssue() {
        return issue;
    }

    public List<HistoryEvent> getIssueEvents() {
        return issueEvents;
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

    public MessageDTO getSelectedMessage() {
        return selectedMessage;
    }

    public void setSelectedMessage(MessageDTO selectedMessage) {
        this.selectedMessage = selectedMessage;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}