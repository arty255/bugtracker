package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.dao.IssueDAO;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueDTO;
import org.hetsold.bugtracker.model.IssueShortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IssueConverter {
    private IssueDAO issueDAO;

    @Autowired
    public IssueConverter(IssueDAO issueDAO) {
        this.issueDAO = issueDAO;
    }

    public Issue getIssue(IssueShortDTO issueShortDTO) {
        if (issueShortDTO != null && !issueShortDTO.getUuid().isEmpty()) {
            return issueDAO.getIssueById(issueShortDTO.getUuid());
        }
        return null;
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
