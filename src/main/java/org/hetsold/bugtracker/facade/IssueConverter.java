package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.springframework.stereotype.Component;

@Component
public class IssueConverter {

    public Issue getIssue(IssueDTO issueDTO) {
        Issue issue = null;
        if (issueDTO != null) {
            issue = new Issue.Builder()
                    .withIssueUuid(issueDTO.getUuid())
                    .withIssueNumber(issueDTO.getIssueNumber())
                    .withDescription(issueDTO.getDescription())
                    .withCreationTime(issueDTO.getCreationTime())
                    .withProductVersion(issueDTO.getProductVersion())
                    .withIssueExistedResult(issueDTO.getExistedResult())
                    .withIssueExpectedResult(issueDTO.getExpectedResult())
                    .withIssueSeverity(issueDTO.getSeverity())
                    .withFixVersion(issueDTO.getFixVersion())
                    .withIssueState(issueDTO.getCurrentState())
                    .build();
        }
        return issue;
    }

    public Issue getIssue(IssueShortDTO issueShortDTO) {
        Issue issue = null;
        if (issueShortDTO != null) {
            issue = new Issue.Builder()
                    .withIssueUuid(issueShortDTO.getUuid())
                    .withIssueNumber(issueShortDTO.getIssueNumber())
                    .withDescription(issueShortDTO.getDescription())
                    .withCreationTime(issueShortDTO.getCreationTime())
                    .withIssueState(issueShortDTO.getCurrentState())
                    .withIssueSeverity(issueShortDTO.getSeverity())
                    .build();
        }
        return issue;
    }

    public Issue getUnsavedIssue(IssueDTO issueDTO) {
        return new Issue.Builder()
                .withIssueNumber(issueDTO.getIssueNumber())
                .withDescription(issueDTO.getDescription())
                .withCreationTime(issueDTO.getCreationTime())
                .withReproduceSteps(issueDTO.getReproduceSteps())
                .withIssueExistedResult(issueDTO.getExistedResult())
                .withIssueExpectedResult(issueDTO.getExpectedResult())
                .withIssueState(issueDTO.getCurrentState())
                .withIssueSeverity(issueDTO.getSeverity())
                .build();
    }

    public IssueDTO getIssueDTO(Issue issue) {
        return new IssueDTO(issue);
    }

    public IssueShortDTO getIssueShortDTO(Issue issue) {
        return new IssueShortDTO(issue);
    }
}
