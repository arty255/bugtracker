package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;

import java.util.List;

public interface IssueDAO {
    void save(Issue issue);

    List<Issue> listAll();

    List<Issue> getIssueList(int startPosition, int limit);

    void delete(Issue issue);

    long getIssueCount();

    Issue getIssueById(String uuid);

    List<Issue> getIssueByCriteria(Issue issue);

    Issue getIssueToDetailedViewById(String uuid);
}
