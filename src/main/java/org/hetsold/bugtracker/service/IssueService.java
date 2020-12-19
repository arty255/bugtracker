package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;

import java.util.List;

public interface IssueService {
    void save(Issue issue);

    Issue getIssueById(String uuid);

    void deleteIssue(Issue issue);

    List<Issue> findIssueByCriteria(Issue issue);

    Issue getIssueForViewById(String uuid);

    //issue - selected issue to change State
    //newState - new issue state
    //assignedTo - user to who issue will be assigned
    void changeIssueState(Issue issue, State newState, User assignedTo);

    void addIssueMessage(Issue issue, Message message);

    void createIssue(Issue issue, User user);

    void createIssueFromTicket(Ticket ticket, User user);

    void updateIssueState(Issue issue, User user);
}
