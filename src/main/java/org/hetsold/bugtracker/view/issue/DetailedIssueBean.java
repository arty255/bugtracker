package org.hetsold.bugtracker.view.issue;


import org.hetsold.bugtracker.dto.*;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.UserService;
import org.hetsold.bugtracker.view.ListableMessageBean;
import org.hetsold.bugtracker.view.user.UserDTOLazyDataModel;
import org.primefaces.model.LazyDataModel;
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
public class DetailedIssueBean extends ListableMessageBean implements Serializable {
    private String uuid;
    private IssueDTO issue;
    @Autowired
    private IssueService issueService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    private HistoryEventLazyDataModel historyEventDataModel;

    private boolean isOriginalStateChanged;
    private IssueState originalIssueState;

    private LazyDataModel<UserDTO> userDTODataModel;
    private UserDTO selectedToAssignUser;

    @PostConstruct
    public void initBean() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        isOriginalStateChanged = false;
        selectedToAssignUser = null;
        userDTODataModel = new UserDTOLazyDataModel(userService);
        activeUser = ((SecurityUserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
        initMessageListener();
    }

    public void preInitIssue() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            reloadIssue();
            originalIssueState = issue.getCurrentIssueState();
            initHistory();
            initMessageListener();
        }
    }

    private void reloadIssue() {
        issue = issueService.getIssueDTOById(uuid);
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
        reloadIssue();
    }

    public void unAssignUser() {
        issueService.changeIssueAssignedUser(issue, null, activeUser);
        reloadIssue();
    }

    public void reAssignUser() {
        issueService.changeIssueAssignedUser(issue, selectedToAssignUser, activeUser);
        reloadIssue();
    }

    @Override
    public void preformUpdateOperation(MessageDTO messageDTO) {
        messageService.saveOrUpdateMessage(messageDTO, activeUser);
    }

    @Override
    public void preformSaveOperation(MessageDTO messageDTO) {
        issueService.addIssueMessage(issue, messageDTO, activeUser);
    }

    @Override
    public void preformDeleteOperation(MessageDTO messageDTO) {
        issueService.deleteIssueMessage(messageDTO);
    }

    public void archiveIssueAction() {
        issueService.makeIssueArchived(new IssueShortDTO(issue.getUuid()), activeUser);
        reloadIssue();
    }

    public void unarchiveAction() {
        issueService.makeIssueUnArchived(new IssueShortDTO(issue.getUuid()), activeUser);
        reloadIssue();
    }

    public String deleteIssueAction() {
        issueService.deleteIssue(new Issue.Builder().withIssueUuid(issue.getUuid()).build());
        return "issues";
    }

    public void save() {
        issue = issueService.saveOrUpdateIssue(issue, activeUser);
    }

    public void messageDateAscendingInListListener() {
        historyEventDataModel.setInverseDateOrder(false);
    }

    public void messageDateDescendingInListListener() {
        historyEventDataModel.setInverseDateOrder(true);
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

    public HistoryEventLazyDataModel getHistoryEventDataModel() {
        return historyEventDataModel;
    }
}