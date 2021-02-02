package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;
import org.hetsold.bugtracker.model.filter.Contract;
import org.hetsold.bugtracker.service.TicketService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class TicketLazyDataModel extends LazyDataModel<TicketDTO> {
    private TicketService ticketService;
    private UserDTO selectedUser;
    private Contract contract;

    public TicketLazyDataModel(TicketService ticketService, UserDTO selectedUser) {
        this.ticketService = ticketService;
        this.selectedUser = selectedUser;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setSelectedUser(UserDTO userDTO) {
        this.selectedUser = userDTO;
    }

    @Override
    public List<TicketDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        if (selectedUser != null) {
            this.setRowCount((int) ticketService.getTicketCountReportedByUser(selectedUser, contract));
            return ticketService.getTicketDTOListReportedByUser(selectedUser, contract, first, pageSize);
        } else {
            this.setRowCount((int) ticketService.getTicketsCount(contract));
            return ticketService.getTicketDTOList(contract, first, pageSize);
        }
    }


    @Override
    public Object getRowKey(TicketDTO ticket) {
        return ticket.getUuid();
    }
}