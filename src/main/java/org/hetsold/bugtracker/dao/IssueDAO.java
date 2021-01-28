package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.filter.Contract;

import java.util.List;

public interface IssueDAO {
    void save(Issue issue);

    List<Issue> listAll();

    List<Issue> getIssueList(Issue issue, int startPosition, int limit);

    List<Issue> getIssueList(Contract contract, int startPosition, int limit);

    void delete(Issue issue);

    long getIssueCount(Issue issue);

    Issue getIssueById(String uuid);

    List<Issue> getIssueByCriteria(Issue issue);

    Issue getIssueToDetailedViewById(String uuid);
}
