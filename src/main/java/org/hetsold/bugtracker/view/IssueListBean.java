package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;


import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class IssueListBean implements Serializable {
    private LazyDataModel<IssueShortDTO> issuesLazyDataModel;
    @Autowired
    private IssueService issueService;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initIssueList();
    }

    public void initIssueList() {
        issuesLazyDataModel = new IssuesLazyDataModel(issueService);
    }

    public LazyDataModel<IssueShortDTO> getIssuesLazyDataModel() {
        return issuesLazyDataModel;
    }
}