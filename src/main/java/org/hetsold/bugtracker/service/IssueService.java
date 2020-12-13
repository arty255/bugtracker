package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;

public interface IssueService {
    void save(Issue issue);

    Issue getIssueById(String uuid);

    void deleteIssue(Issue issue);
}
