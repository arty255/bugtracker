package org.hetsold.bugtracker.model;

import java.util.Date;

public class IssueShortDTO {
    private String uuid;
    private String issueNumber;
    private String description;
    private Date creationTime;
    private State currentState;
    private Severity severity;
    private User reportedBy;
    private User assignedTo;
    private String ticketId;

    public IssueShortDTO(Issue issue) {
        this.uuid = issue.getUuid();
        this.issueNumber = issue.getIssueNumber();
        this.description = issue.getDescription();
        this.creationTime = issue.getCreationTime();
        this.currentState = issue.getCurrentState();
        this.severity = issue.getSeverity();
        this.reportedBy = issue.getReportedBy();
        this.assignedTo = issue.getAssignedTo();
        if (issue.getTicket().getUuid() != null) {
            this.ticketId = issue.getTicket().getUuid();
        }
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

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
