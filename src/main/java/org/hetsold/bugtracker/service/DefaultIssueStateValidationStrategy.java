package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueState;
import org.springframework.stereotype.Component;

@Component
public class DefaultIssueStateValidationStrategy implements IssueStateValidationStrategy {
    @Override
    public boolean isValid(Issue issue) {
        if (isOpenOrReopen(issue) && issue.getAssignedTo() != null) {
            return false;
        }
        return !isAssignedOrFixed(issue) || issue.getAssignedTo() != null;
    }

    private boolean isOpenOrReopen(Issue issue) {
        return issue.getCurrentIssueState() == IssueState.OPEN || issue.getCurrentIssueState() == IssueState.REOPEN;
    }

    private boolean isAssignedOrFixed(Issue issue) {
        return issue.getCurrentIssueState() == IssueState.FIXED || issue.getCurrentIssueState() == IssueState.ASSIGNED;
    }
}
