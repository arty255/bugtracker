package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface IssueFacade {
    void createIssue(IssueDTO issueDTO, UserDTO userDTO);

    void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO);

    void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO);

    List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO);

    void deleteIssue(String issueUUID, boolean includeTicket);
}
