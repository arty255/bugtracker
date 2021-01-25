package org.hetsold.bugtracker.view;

import org.hetsold.bugtracker.model.MessageDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class TicketMessageLazyDataModel extends LazyDataModel<MessageDTO> {
    private boolean inverseDateOrder;
    private TicketService ticketService;
    private MessageService messageService;
    private TicketDTO ticket;

    public TicketMessageLazyDataModel(TicketService ticketService, MessageService messageService, TicketDTO ticket) {
        this.ticketService = ticketService;
        this.messageService = messageService;
        this.ticket = ticket;
    }

    public void setInverseDateOrder(boolean inverseDateOrder) {
        this.inverseDateOrder = inverseDateOrder;
    }

    @Override
    public List<MessageDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) ticketService.getMessagesCountByTicket(ticket));
        return ticketService.getTicketMessages(ticket, first, pageSize, inverseDateOrder);
    }

    @Override
    public MessageDTO getRowData(String rowKey) {
        return messageService.getMessageDTOById(rowKey);
    }

    @Override
    public Object getRowKey(MessageDTO object) {
        return object.getUuid();
    }
}
