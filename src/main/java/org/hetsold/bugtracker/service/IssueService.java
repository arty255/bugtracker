package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.model.filter.Contract;

import java.util.List;

public interface IssueService {

    void setStateValidationStrategy(IssueStateValidationStrategy stateValidationStrategy);

    Issue saveOrUpdateIssue(Issue issue, User user);

    IssueDTO saveOrUpdateIssue(IssueDTO issueDTO, UserDTO userDTO);

    void changeIssueArchiveState(Issue issue, User user, boolean newState);

    void makeIssueArchived(IssueShortDTO issueShortDTO, UserDTO userDTO);

    void makeIssueUnArchived(IssueShortDTO issueShortDTO, UserDTO userDTO);

    Issue getIssueById(String uuid);

    IssueDTO getIssueDTOById(String uuid);

    void deleteIssue(Issue issue);

    List<IssueShortDTO> getIssueList(Contract contract, int startPosition, int limit);

    long getIssuesCount(Contract contract);

    void changeIssueState(Issue issue, IssueState newIssueState, User user);

    void changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO);

    void changeIssueAssignedUser(Issue issue, User assignedTo, User user);

    void changeIssueAssignedUser(IssueDTO issueDTO, UserDTO assignedTo, UserDTO userDTO);

    void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO, UserDTO userDTO);

    void addIssueMessage(Issue issue, Message message, User user);

    void createNewIssue(Issue issue, User user);

    Issue createIssueFromTicket(Ticket ticket, User user);

    IssueShortDTO createIssueFromTicket(TicketDTO selectedTicketDTO, UserDTO user);

    void assignIssueToTicket(IssueDTO issueDTO, TicketDTO ticketDTO);

    List<IssueEvent> getIssueEvents(Issue issue, int startPosition, int limit, boolean inverseDateOrder);

    List<IssueEventDTO> getIssueHistoryEventsDTO(IssueDTO issueDTO, int startPosition, int limit, boolean inverseDateOrder);

    long getIssueHistoryEventsCount(Issue issue);

    long getIssueHistoryEventsCount(IssueDTO issueDTO);

    void deleteIssueMessage(MessageDTO selectedToDeleteMessage);

}
