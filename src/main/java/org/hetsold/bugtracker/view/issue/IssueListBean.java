package org.hetsold.bugtracker.view.issue;

import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.view.filter.ContractBuilder;
import org.hetsold.bugtracker.view.filter.DisplayableFieldFilter;
import org.hetsold.bugtracker.view.filter.FilterComponentBuilder;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class IssueListBean implements Serializable {
    private LazyDataModel<IssueShortDTO> issuesLazyDataModel;
    private IssueShortDTO issue;
    @Autowired
    private IssueService issueService;
    private UserDTO activeUser;

    private List<DisplayableFieldFilter> displayableFieldFilters;

    @PostConstruct
    public void init() {
        FacesContextUtils.getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initIssueList();
        createIssueFilterWrappersAction();
        activeUser = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserDTO();
    }

    public void initIssueList() {
        issuesLazyDataModel = new IssuesLazyDataModel(issueService);
    }

    public LazyDataModel<IssueShortDTO> getIssuesLazyDataModel() {
        return issuesLazyDataModel;
    }

    public void createIssueFilterWrappersAction() {
        displayableFieldFilters = FilterComponentBuilder.buildWrappers(IssueShortDTO.class,
                "uuid description currentIssueState severity archived");
    }

    public void modelFiltersUpdateAction() {
        ((IssuesLazyDataModel) issuesLazyDataModel).setContract(ContractBuilder.buildContact(displayableFieldFilters));
    }

    public void preformArchiveAction() {
        issueService.makeIssueArchived(issue);
    }

    public void preformDeleteAction() {
        issueService.deleteIssue(new IssueDTO(issue.getUuid()));
    }

    public void preformUnarchiveAction() {
        issueService.makeIssueUnarchived(issue);
    }

    public IssueShortDTO getIssue() {
        return issue;
    }

    public void setIssue(IssueShortDTO issue) {
        this.issue = issue;
    }

    public List<DisplayableFieldFilter> getIssueFilterList() {
        return displayableFieldFilters;
    }
}