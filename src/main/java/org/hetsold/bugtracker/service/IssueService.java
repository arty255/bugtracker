package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;

import java.util.List;

public interface IssueService {
    void save(Issue issue);

    Issue getIssueById(String uuid);

    void deleteIssue(Issue issue);

    List<Issue> findIssueByCriteria(Issue issue);

    Issue getIssueForViewById(String uuid);

    void changeIssueState(Issue issue, State state, User user);
}
