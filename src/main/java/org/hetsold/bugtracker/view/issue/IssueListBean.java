package org.hetsold.bugtracker.view.issue;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.user.SecurityUserDetails;
import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.view.filter.*;
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
import java.util.Set;

@ManagedBean
@ViewScoped
public class IssueListBean implements Serializable {
    private LazyDataModel<IssueShortDTO> issuesLazyDataModel;
    private IssueShortDTO issue;
    @Autowired
    private IssueService issueService;
    private UserDTO activeUser;

    private Set<FieldMaskFilter> fieldMaskFilters;

    private String columnKey;
    private SortedColumnsContainer sortedColumnsContainer;
    private Contract contract;

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
        fieldMaskFilters = FilterComponentBuilder.buildFieldMaskFilters(IssueShortDTO.class,
                "uuid description currentIssueState severity archived");
        sortedColumnsContainer = new SortedColumnsContainer(FilterComponentBuilder.buildFieldOrderFilters(IssueShortDTO.class, "currentIssueState severity creationTime"));
    }

    private void buildContract() {
        contract = ContractBuilder.buildContact(fieldMaskFilters, sortedColumnsContainer.getFinalOrderFilters());
    }

    public void modelFiltersUpdateAction() {
        ((IssuesLazyDataModel) issuesLazyDataModel).setContract(ContractBuilder.buildContact(fieldMaskFilters, sortedColumnsContainer.getFinalOrderFilters()));
    }

    public void changeSortOrderAction() {
        sortedColumnsContainer.twoStateToggle(columnKey);
        updateDataModelContract();
    }

    public void cancelSortedOrderAction() {
        sortedColumnsContainer.removeColumnNameFromOrder(columnKey);
        updateDataModelContract();
    }

    private void updateDataModelContract() {
        buildContract();
        ((IssuesLazyDataModel) issuesLazyDataModel).setContract(contract);
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

    public Set<FieldMaskFilter> getIssueFilterList() {
        return fieldMaskFilters;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public int getColumnKeyIndex(String columnKey) {
        return sortedColumnsContainer.getColumnKeyIndex(columnKey);
    }

    public boolean columnInOrderFilters(String columnKey) {
        return sortedColumnsContainer.containsColumnKeyInFilters(columnKey);
    }

    public boolean isAscending(String columnKey) {
        return sortedColumnsContainer.isAscending(columnKey);
    }
}