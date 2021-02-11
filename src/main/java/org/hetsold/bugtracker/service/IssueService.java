package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dto.*;
import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.IssueState;

import java.util.List;

public interface IssueService {
    IssueDTO createNewIssue(IssueDTO issueDTO);

    IssueDTO updateIssue(IssueDTO issueDTO, UserDTO userDTO);

    IssueShortDTO createIssueFromTicket(TicketDTO selectedTicketDTO, UserDTO user);

    void assignIssueToTicket(IssueShortDTO issueShortDTO, TicketDTO ticketDTO);

    void changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO);

    void changeIssueAssignedUser(IssueDTO issueDTO, UserDTO assignedTo, UserDTO userDTO);

    void makeIssueArchived(IssueShortDTO issueShortDTO);

    void makeIssueUnarchived(IssueShortDTO issueShortDTO);

    IssueDTO getIssueDTO(IssueDTO issueDTO);

    IssueShortDTO getIssueShortDTO(IssueShortDTO issueShortDTO);

    void deleteIssue(IssueDTO issueDTO);

    List<IssueShortDTO> getIssueList(Contract contract, int startPosition, int limit);

    long getIssuesCount(Contract contract);

    List<IssueEventDTO> getIssueEventsDTO(IssueDTO issueDTO, int startPosition, int limit, boolean inverseDateOrder);

    long getIssueEventsCount(IssueDTO issueDTO);

    void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO);

    void deleteIssueMessage(MessageDTO messageDTO);
}