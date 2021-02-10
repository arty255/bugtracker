package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Ticket;
import org.hetsold.bugtracker.model.User;

public interface IssueServiceInternal {

    void createNewIssue(Issue issue);

    Issue updateIssue(Issue issue, User user);

    Issue createIssueFromTicket(Ticket ticket, User editor);

    void assignIssueToTicket(Issue issue, Ticket ticket);

    void changeIssueState(Issue issue, IssueState newIssueState, User editor);

    void changeIssueAssignedUser(Issue issue, User assignedTo, User editor);

    void changeIssueArchiveState(Issue issue, boolean newState);

    Issue getIssue(Issue issue);

    void delete(Issue issue);
}