package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.hetsold.bugtracker.model.TicketDTO;
import org.hetsold.bugtracker.model.UserDTO;

import java.util.List;

public interface IssueFacade {
    void createIssue(Issue issue, UserDTO userDTO);

    void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO);

    void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO);

    List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO);

    void deleteIssue(String issueUUID, boolean includeTicket);
}
