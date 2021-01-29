package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.model.filter.Contract;

import java.util.List;

public interface IssueService {
    void save(Issue issue);

    Issue updateIssue(Issue issue);

    Issue saveOrUpdateIssue(Issue issue, User user);

    IssueDTO saveOrUpdateIssue(IssueDTO issueDTO, UserDTO userDTO);

    void changeIssueArchiveState(Issue issue, User user, boolean newState);

    void makeIssueArchived(IssueShortDTO issueShortDTO, UserDTO userDTO);

    void makeIssueUnArchived(IssueShortDTO issueShortDTO, UserDTO userDTO);

    Issue getIssueById(String uuid);

    IssueDTO getIssueDTOById(String uuid);

    void deleteIssue(Issue issue);

    List<Issue> findIssueByFilter(Issue issue);

    List<IssueShortDTO> getIssueList(IssueDTO issue, int startPosition, int limit);

    List<IssueShortDTO> getIssueList(Contract contract, int startPosition, int limit);

    long getIssuesCount(IssueDTO issue);

    long getIssuesCount(Contract contract);

    Issue getIssueForViewById(String uuid);

    //issue - selected issue to change State
    //newState - new issue state
    //assignedTo - user to who issue will be assigned
    void changeIssueState(Issue issue, IssueState newIssueState, User user);

    void changeIssueState(IssueDTO issueDTO, IssueState newIssueState, UserDTO userDTO);

    void changeIssueAssignedUser(Issue issue, User assignedTo, User user);

    void changeIssueAssignedUser(IssueDTO issueDTO, UserDTO assignedTo, UserDTO userDTO);

    void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO, UserDTO userDTO);

    void addIssueMessage(Issue issue, Message message, User user);

    void createNewIssue(Issue issue, User user);

    Issue createIssueFromTicket(Ticket ticket, User user);

    IssueShortDTO createIssueFromTicket(TicketDTO selectedTicketDTO, UserDTO user);

    void updateIssueState(Issue issue, User user);

    List<IssueEvent> getIssueEvents(Issue issue, int startPosition, int limit, boolean inverseDateOrder);

    List<IssueEventDTO> getIssueHistoryEventsDTO(IssueDTO issueDTO, int startPosition, int limit, boolean inverseDateOrder);

    long getIssueHistoryEventsCount(Issue issue);

    long getIssueHistoryEventsCount(IssueDTO issueDTO);

    void deleteIssueMessage(MessageDTO selectedToDeleteMessage);
}
