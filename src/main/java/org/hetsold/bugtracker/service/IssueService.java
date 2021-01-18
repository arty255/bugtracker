package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface IssueService {
    void save(Issue issue);

    void generateAndSaveIssue();

    Issue getIssueById(String uuid);

    void deleteIssue(Issue issue);

    List<Issue> findIssueByFilter(Issue issue);

    List<Issue> getIssueList();

    Issue getIssueForViewById(String uuid);

    //issue - selected issue to change State
    //newState - new issue state
    //assignedTo - user to who issue will be assigned
    void changeIssueState(Issue issue, State newState, User user);

    void changeIssueAssignedUser(Issue issue, User assignedTo, User user);

    void addIssueMessage(IssueDTO issueDTO, MessageDTO messageDTO, UserDTO userDTO);

    void addIssueMessage(Issue issue, Message message, User user);

    void createNewIssue(Issue issue, User user);

    Issue createIssueFromTicket(Ticket ticket, User user);

    IssueShortDTO createIssueFromTicket(TicketDTO selectedTicketDTO, UserDTO user);

    void updateIssueState(Issue issue, User user);
}
