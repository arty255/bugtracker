package org.hetsold.bugtracker.view.ticket;

import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.service.MessageService;
import org.hetsold.bugtracker.service.TicketService;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.util.List;
import java.util.Map;

public class TicketMessageLazyDataModel extends LazyDataModel<MessageDTO> {
    private static final long serialVersionUID = 222458217491597163L;
    private boolean inverseDateOrder;
    private final transient TicketService ticketService;
    private final transient MessageService messageService;
    private final TicketDTO ticket;

    public TicketMessageLazyDataModel(TicketService ticketService, MessageService messageService, TicketDTO ticket) {
        this.ticketService = ticketService;
        this.messageService = messageService;
        this.ticket = ticket;
    }

    public void setInverseDateOrder(boolean inverseDateOrder) {
        this.inverseDateOrder = inverseDateOrder;
    }

    public boolean isInverseDateOrder() {
        return inverseDateOrder;
    }

    @Override
    public List<MessageDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        this.setRowCount((int) ticketService.getMessagesCountByTicket(ticket));
        return ticketService.getTicketMessages(ticket, first, pageSize, inverseDateOrder);
    }

    @Override
    public MessageDTO getRowData(String rowKey) {
        return messageService.getMessage(new MessageDTO(rowKey));
    }

    @Override
    public Object getRowKey(MessageDTO object) {
        return object.getUuid();
    }
}
