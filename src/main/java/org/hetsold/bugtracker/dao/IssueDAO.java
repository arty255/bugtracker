package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.Issue;

import java.util.List;

public interface IssueDAO {
    void save(Issue issue);

    List<Issue> getIssueList(Contract contract, int startPosition, int limit);

    long getIssueCount(Contract contract);

    void delete(Issue issue);

    Issue getIssueById(String uuid);

    Issue getIssueToDetailedViewById(String uuid);
}
