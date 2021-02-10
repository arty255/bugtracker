package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface MessageServiceInternal {

    void saveNewMessage(Message message);

    Message updateMessage(Message message, User messageEditor);

    void delete(Message message);

    Message getMessage(Message message);

    List<Message> getMessagesForTicket(Ticket ticketDTO, int startPosition, int limit, boolean inverseDateOrder);

    long getMessagesCountForTicket(Ticket ticket);
}