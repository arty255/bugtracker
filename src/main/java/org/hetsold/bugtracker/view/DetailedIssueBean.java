package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.model.HistoryEvent;
import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void initBean() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public void preInitIssue() {
        issue = issueService.getIssueDTOById(uuid);
        initEvents();
    }

    public void initEvents() {
        issueEvents = issueService.getIssueEvents(issue, 0, 0);
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
}