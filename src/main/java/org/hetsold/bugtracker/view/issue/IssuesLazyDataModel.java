package org.hetsold.bugtracker.view.issue;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class IssuesLazyDataModel extends LazyDataModel<IssueShortDTO> {
    private static final long serialVersionUID = 3533587834521806709L;
    private final transient IssueService issueService;
    private transient Contract contract;

    public IssuesLazyDataModel(IssueService issueService) {
        this.issueService = issueService;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    @Override
    public Object getRowKey(IssueShortDTO issue) {
        return issue.getUuid();
    }

    @Override
    public List<IssueShortDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) issueService.getIssuesCount(contract));
        return issueService.getIssueList(contract, first, pageSize);
    }
}
