package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.UserDTO;
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
        /*todo: change with spring security integration*/
        this.activeUser = userService.getUserDTOById("1b1ef410-2ad2-4ac2-ab16-9707bd026e06");
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