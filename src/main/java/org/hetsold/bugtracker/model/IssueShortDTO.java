package org.hetsold.bugtracker.model;

import java.util.Date;

public class IssueShortDTO {
    private String uuid;
    private String issueNumber;
    private String description;
    private Date creationTime;
    private State currentState;
    private Severity severity;
    private UserDTO reportedBy;
    private UserDTO assignedTo;
    private String ticketId;

    public IssueShortDTO(Issue issue) {
        this.uuid = issue.getUuid();
        this.issueNumber = issue.getIssueNumber();
        this.description = issue.getDescription();
        this.creationTime = issue.getCreationTime();
        this.currentState = issue.getCurrentState();
        this.severity = issue.getSeverity();
        if (issue.getAssignedTo() != null) {
            this.reportedBy = new UserDTO(issue.getReportedBy());
        }
        if (issue.getAssignedTo() != null) {
            this.assignedTo = new UserDTO(issue.getAssignedTo());
        }
        if (issue.getTicket() != null) {
            this.ticketId = issue.getTicket().getUuid();
        }
    }

    public IssueShortDTO() {
        this("");
    }

    public IssueShortDTO(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
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
