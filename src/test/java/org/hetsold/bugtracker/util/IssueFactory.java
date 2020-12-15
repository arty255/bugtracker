package org.hetsold.bugtracker.util;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.State;
import org.hetsold.bugtracker.model.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class IssueFactory {
    private User firstUser;

    public IssueFactory(User firstUser) {
        this.firstUser = firstUser;
    }

    public synchronized Issue getIssue(IssueFactoryCreatedIssueType issueFactoryCreatedIssueType) {
        Date date;
        Issue issue = new Issue.Builder()
                .withIssueId("issue number 1")
                .withIssueAppearanceTime(new Date())
                .withTicketCreationTime(new Date())
                .withProductVersion("product version v0.1")
                .withShortDescription("short description")
                .withFullDescription("full description")
                .withReproduceSteps("1.step 1, 2.step 2")
                .withReportedBy(firstUser)
                .build();
        switch (issueFactoryCreatedIssueType) {
            case CorrectIssue:
                break;
            case CorrectOpenIssue:
                issue.setCurrentState(State.OPEN);
                break;
            case CorrectClosedIssue:
                issue.setCurrentState(State.FIXED);
                break;
            case InvalidAppearanceDateIssue:
                issue.setIssueAppearanceTime(Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant()));
                break;
            case InvalidCreationDateIssue:
                issue.setTicketCreationTime(Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant()));
                break;
            case InvalidUserIssue:
                issue.setReportedBy(null);
                break;
            case InvalidShortAndFullDescriptionIssue:
                issue.setFullDescription("");
                issue.setShortDescription("");
                break;
            case CorrectDayAgoIssue:
                date = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
                issue.setIssueAppearanceTime(date);
                issue.setTicketCreationTime(date);
                break;
            case CorrectWeekAgoIssue:
                date = Date.from(LocalDateTime.now().minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant());
                issue.setTicketCreationTime(date);
                issue.setIssueAppearanceTime(date);
                break;
            case CorrectTwoWeekAgoIssue:
                date = Date.from(LocalDateTime.now().minusWeeks(2).atZone(ZoneId.systemDefault()).toInstant());
                issue.setTicketCreationTime(date);
                issue.setIssueAppearanceTime(date);
                break;
            case CorrectMountAgoIssue:
                date = Date.from(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());
                issue.setTicketCreationTime(date);
                issue.setIssueAppearanceTime(date);
                break;
        }
        return issue;
    }
}
