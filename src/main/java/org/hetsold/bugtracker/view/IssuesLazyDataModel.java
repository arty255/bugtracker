package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class IssuesLazyDataModel extends LazyDataModel<IssueShortDTO> {
    private IssueService issueService;
    private IssueDTO filterIssue;

    public IssuesLazyDataModel(IssueService issueService) {
        this.issueService = issueService;
        this.filterIssue = new IssueDTO();
    }

    public void setFilterIssue(IssueDTO filterIssue) {
        this.filterIssue = filterIssue;
    }

    @Override
    public Object getRowKey(IssueShortDTO issue) {
        return issue.getUuid();
    }

    @Override
    public List<IssueShortDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) issueService.getIssuesCount(filterIssue));
        return issueService.getIssueList(filterIssue, first, pageSize);
    }
}
