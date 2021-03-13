package org.hetsold.bugtracker.view.issue;

import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueEventDTO;
import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class HistoryEventLazyDataModel extends LazyDataModel<IssueEventDTO> {
    private static final long serialVersionUID = -6275313689757318349L;
    private final transient IssueService issueService;
    private final transient MessageService messageService;
    private final IssueDTO issueDTO;
    private boolean inverseDateOrder;

    public HistoryEventLazyDataModel(final IssueService issueService, final MessageService messageService, final IssueDTO issueDTO) {
        this.issueService = issueService;
        this.messageService = messageService;
        this.issueDTO = issueDTO;
    }

    public boolean isInverseDateOrder() {
        return inverseDateOrder;
    }

    public void setInverseDateOrder(final boolean inverseDateOrder) {
        this.inverseDateOrder = inverseDateOrder;
    }

    @Override
    public List<IssueEventDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) issueService.getIssueEventsCount(issueDTO));
        return issueService.getIssueEventsDTO(issueDTO, first, pageSize, inverseDateOrder);
    }

    @Override
    public IssueEventDTO getRowData(String rowKey) {
        if (!rowKey.isEmpty()) {
            return new IssueEventDTO(IssueEventDTO.EventType.MESSAGE_EVENT
                    , messageService.getMessage(new MessageDTO(rowKey)));
        } else {
            return null;
        }
    }

    @Override
    public String getRowKey(IssueEventDTO object) {
        if (object.getEventType() == IssueEventDTO.EventType.MESSAGE_EVENT) {
            return ((MessageDTO) object.getEntityDTO()).getUuid();
        }
        return "";
    }
}
