package org.hetsold.bugtracker.view.issue;

import org.hetsold.bugtracker.dto.FullUserDetails;
import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class AddIssueBean implements Serializable {
    @Autowired
    private IssueService issueService;
    @Autowired
    private UserService userService;

    private IssueDTO issue;
    private List<UserDTO> userList;
    private UserDTO activeUser;

    @PostConstruct
    public void preInit() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        activeUser = ((FullUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
        userList = new ArrayList<>();
        initIssueListener();
    }

    public void initIssueListener() {
        issue = new IssueDTO();
        userList = userService.getUsers(null, 0, Integer.MAX_VALUE, false);
    }

    public void addIssueAction() {
        issue.setReportedBy(activeUser);
        issueService.createNewIssue(issue);
    }

    public void issueStateChangeListener() {
        if (isIssueAssignOrFixed() && issue.getAssignedTo() == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "User need to be assigned", "User need to be assigned"));
        }
    }

    public void issueAssignationChangeListener() {
        UserDTO assignedTo = issue.getAssignedTo();
        if (assignedTo != null && isIssueOpenOrReopen()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "State need to be ASSIGNED or FIXED", "State need to be ASSIGNED or FIXED"));
        } else {
            issueStateChangeListener();
        }
    }

    public boolean isIssueAssignOrFixed() {
        return issue.getCurrentIssueState() == IssueState.ASSIGNED || issue.getCurrentIssueState() == IssueState.FIXED;
    }

    private boolean isIssueOpenOrReopen() {
        return issue.getCurrentIssueState() == IssueState.OPEN || issue.getCurrentIssueState() == IssueState.REOPEN;
    }

    public IssueDTO getIssue() {
        return issue;
    }

    public void setIssue(IssueDTO issue) {
        this.issue = issue;
    }

    public List<UserDTO> getUserList() {
        return userList;
    }
}