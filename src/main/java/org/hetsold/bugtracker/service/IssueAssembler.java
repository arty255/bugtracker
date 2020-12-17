package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.rest.IssueShortDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class IssueAssembler {
    private IssueService issueService;

    @Autowired
    public IssueAssembler(IssueService issueService) {
        this.issueService = issueService;
    }

    public IssueShortDTO getIssueShortDTO(Issue issue) {
        IssueShortDTO issueShortDTO = new IssueShortDTO();
        issueShortDTO.setUuid(issue.getUuid());
        issueShortDTO.setIssueId(issue.getUuid());
        issueShortDTO.setDescription(issue.getFullDescription());
        issueShortDTO.setState(issue.getCurrentState());
        issueShortDTO.setDateOfCreation(issue.getTicketCreationTime());
        return issueShortDTO;
    }

    public Issue getIssue(IssueShortDTO issueShortDTO) {
        return Objects.nonNull(issueShortDTO.getUuid()) ? issueService.getIssueById(issueShortDTO.getUuid()) : null;
    }
}
