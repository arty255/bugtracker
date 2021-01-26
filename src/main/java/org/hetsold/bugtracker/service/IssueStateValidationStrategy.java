package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;

public interface IssueStateValidationStrategy {
    boolean isValid(Issue issue);
}
