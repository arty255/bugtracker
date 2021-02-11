package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;

import java.util.List;

public interface MessageService {

    void saveNewMessage(MessageDTO messageDTO);

    void updateMessage(MessageDTO messageDTO, UserDTO messageEditorDto);

    void delete(MessageDTO messageDTO);

    MessageDTO getMessage(MessageDTO messageDTO);

    List<MessageDTO> getMessagesForTicket(TicketDTO ticketDTO, int startPosition, int limit, boolean inverseDateOrder);

    long getMessagesCountForTicket(TicketDTO ticketDTO);
}