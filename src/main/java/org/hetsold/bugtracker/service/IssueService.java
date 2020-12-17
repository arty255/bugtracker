package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface IssueService {
    void save(Issue issue);

    void generateAndSaveIssue();

    Issue getIssueById(String uuid);

    void deleteIssue(Issue issue);

    List<Issue> findIssueByCriteria(Issue issue);

    List<Issue> getIssueList();

    Issue getIssueForViewById(String uuid);

    //issue - selected issue to change State
    //newState - new issue state
    //assignedTo - user to who issue will be assigned
    void changeIssueState(Issue issue, State newState, User assignedTo);

    void addIssueMessage(Issue issue, Message message);

    void changeIssueMessageContent(Message message, Message newMessage);

    void deleteMessage(Message message);
}
