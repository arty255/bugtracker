package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Severity;

import java.io.Serializable;
import java.util.Date;

public class IssueDTO extends ArchivedIdentityDTO implements Serializable {
    private static final long serialVersionUID = 2936687026851647779L;
    private String issueNumber;
    private String description;
    private Date creationTime;
    private String productVersion;
    private String reproduceSteps;
    private String existedResult;
    private String expectedResult;
    private Severity severity;
    private String fixVersion;
    private IssueState currentIssueState;
    private UserDTO reportedBy;
    private UserDTO assignedTo;
    private TicketDTO ticket;

    public IssueDTO() {
        super();
    }

    public IssueDTO(final String uuid) {
        super();
        this.setUuid(uuid);
    }

    public IssueDTO(final Issue issue) {
        super();
        if (issue.getUuid() != null) {
            this.setUuid(issue.getUuid().toString());
        }
        this.issueNumber = issue.getIssueNumber();
        this.description = issue.getDescription();
        this.creationTime = issue.getCreationTime();
        this.productVersion = issue.getProductVersion();
        this.reproduceSteps = issue.getReproduceSteps();
        this.existedResult = issue.getExistedResult();
        this.expectedResult = issue.getExpectedResult();
        this.severity = issue.getSeverity();
        this.fixVersion = issue.getFixVersion();
        this.currentIssueState = issue.getCurrentIssueState();
        this.setArchived(issue.getArchived());
        if (issue.getReportedBy() != null) {
            this.reportedBy = new UserDTO(issue.getReportedBy());
        }
        if (issue.getAssignedTo() != null) {
            this.assignedTo = new UserDTO(issue.getAssignedTo());
        }
        if (issue.getTicket() != null) {
            this.ticket = new TicketDTO(issue.getTicket());
        }
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(final String issueNumber) {
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

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public String getReproduceSteps() {
        return reproduceSteps;
    }

    public void setReproduceSteps(String reproduceSteps) {
        this.reproduceSteps = reproduceSteps;
    }

    public String getExistedResult() {
        return existedResult;
    }

    public void setExistedResult(String existedResult) {
        this.existedResult = existedResult;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    public IssueState getCurrentIssueState() {
        return currentIssueState;
    }

    public void setCurrentIssueState(IssueState currentIssueState) {
        this.currentIssueState = currentIssueState;
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

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }
}