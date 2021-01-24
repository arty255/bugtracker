package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.model.IssueEventDTO;
import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.service.IssueService;
import org.hetsold.bugtracker.service.MessageService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class HistoryEventLazyDataModel extends LazyDataModel<IssueEventDTO> {
    private IssueService issueService;
    private MessageService messageService;
    private IssueDTO issueDTO;

    public HistoryEventLazyDataModel(IssueService issueService, MessageService messageService, IssueDTO issueDTO) {
        this.issueService = issueService;
        this.messageService = messageService;
        this.issueDTO = issueDTO;
    }

    @Override
    public List<IssueEventDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) issueService.getIssueHistoryEventsCount(issueDTO));
        return issueService.getIssueHistoryEventsDTO(issueDTO, first, pageSize);
    }

    @Override
    public IssueEventDTO getRowData(String rowKey) {
        if (!rowKey.isEmpty()) {
            return new IssueEventDTO(IssueEventDTO.EventType.MessageEvent
                    , new MessageDTO(messageService.getMessageById(rowKey)));
        } else {
            return null;
        }
    }

    @Override
    public String getRowKey(IssueEventDTO object) {
        if (object.getEventType() == IssueEventDTO.EventType.MessageEvent) {
            return ((MessageDTO) object.getEntityDTO()).getUuid();
        }
        return "";
    }
}
