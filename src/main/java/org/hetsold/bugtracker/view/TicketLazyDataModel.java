package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.service.TicketService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class TicketLazyDataModel extends LazyDataModel<TicketDTO> {
    private TicketService ticketService;
    private UserDTO selectedUser;

    public TicketLazyDataModel(TicketService ticketService, UserDTO selectedUser) {
        this.ticketService = ticketService;
        this.selectedUser = selectedUser;
    }

    @Override
    public List<TicketDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) ticketService.getTicketCountReportedByUser(selectedUser));
        return ticketService.getTicketDtoListReportedByUser(selectedUser, first, pageSize);
    }

    @Override
    public Object getRowKey(TicketDTO ticket) {
        return ticket.getUuid();
    }
}