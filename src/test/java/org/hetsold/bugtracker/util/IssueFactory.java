package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.User;

import java.util.Date;

public class IssueFactory {
    private User savedUser;
    private Issue baseIssue;

    public IssueFactory(Issue baseIssue, User savedUser) {
        this.baseIssue = baseIssue;
        this.savedUser = savedUser;
    }

    public synchronized Issue getIssue(FactoryIssueType factoryIssueType) {
        Issue issue = new Issue.Builder()
                .withIssueNumber("issue number 1")
                .withCreationTime(new Date())
                .withProductVersion("product version v0.1")
                .withDescription("description")
                .withReproduceSteps("1.step 1, 2.step 2")
                .build();
        switch (factoryIssueType) {
            case ISSUE_WITH_PERSISTED_USER:
                issue.setReportedBy(savedUser);
                break;
            case ISSUE_WITH_NOT_PERSISTED_USER:
                issue.setReportedBy(new User("test", "test"));
                break;
            case ISSUE_ON_BASE_OF_PERSISTED:
                issue.setReportedBy(savedUser);
                issue.setUuid(baseIssue.getUuid());
                break;
        }
        return issue;
    }
}