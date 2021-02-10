package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Severity;

import java.util.Date;

public class IssueShortDTO extends ArchivedIdentityDTO {
    private String issueNumber;
    private String description;
    private Date creationTime;
    private IssueState currentIssueState;
    private Severity severity;
    private UserDTO reportedBy;
    private UserDTO assignedTo;
    private String ticketId;

    public IssueShortDTO(Issue issue) {
        this.setUuid(issue.getUuid().toString());
        this.issueNumber = issue.getIssueNumber();
        this.description = issue.getDescription();
        this.creationTime = issue.getCreationTime();
        this.currentIssueState = issue.getCurrentIssueState();
        this.severity = issue.getSeverity();
        this.setArchived(issue.getArchived());
        if (issue.getReportedBy() != null) {
            this.reportedBy = new UserDTO(issue.getReportedBy());
        }
        if (issue.getAssignedTo() != null) {
            this.assignedTo = new UserDTO(issue.getAssignedTo());
        }
        if (issue.getTicket() != null) {
            this.ticketId = issue.getTicket().getUuid().toString();
        }
    }

    public IssueShortDTO() {
        this("");
    }

    public IssueShortDTO(String uuid) {
        this.setUuid(uuid);
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public IssueState getCurrentIssueState() {
        return currentIssueState;
    }

    public void setCurrentIssueState(IssueState currentIssueState) {
        this.currentIssueState = currentIssueState;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public UserDTO getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(UserDTO reportedBy) {
        this.reportedBy = reportedBy;
    }

    public UserDTO getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDTO assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
