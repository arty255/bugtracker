package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dto.IssueDTO;
import org.hetsold.bugtracker.dto.IssueShortDTO;
import org.hetsold.bugtracker.dto.TicketDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;

import java.util.List;

public interface IssueFacade {
    void generateRandomIssue();

    void createIssue(IssueDTO issueDTO);

    void createIssueFromTicket(TicketDTO ticketDTO, UserDTO userDTO);

    void updateIssue(IssueShortDTO issueShortDTO, UserDTO userDTO);

    IssueShortDTO getIssue(String issueUUID);

    List<IssueShortDTO> getIssue(IssueShortDTO issueShortDTO);

    void deleteIssue(String issueUUID, boolean includeTicket);
}
