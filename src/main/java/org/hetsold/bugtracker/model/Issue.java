package org.hetsold.bugtracker.model;

/*
 * A Issue object describe basic data about issue
 */

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BT_ISSUE")
public class Issue extends AbstractIdentity {

    private String issueId;
    private String shortDescription;
    private String fullDescription;
    private Date issueAppearanceTime;
    private Date ticketCreationTime;
    private String productVersion;
    private String reproduceSteps;
    private String existedResult;
    private String expectedResult;
    @ManyToOne
    @JoinTable(name = "BT_ISSUE_REPORTEDBY", joinColumns = @JoinColumn(name = "ISSUEID"), inverseJoinColumns = @JoinColumn(name = "USERID"))
    private User reportedBy;
    @ManyToOne
    @JoinTable(name = "BT_ISSUE_ASSIGNED", joinColumns = @JoinColumn(name = "ISSUEID"), inverseJoinColumns = @JoinColumn(name = "USERID"))
    private User assignedTo;
    private Severity severity;
    //todo: maybe optimize by using aggregator
    private String fixVersion;
    private State currentState;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<HistoryEvent> history;

    public Issue() {
        history = new ArrayList<>();
        currentState = State.OPEN;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shotDescription) {
        this.shortDescription = shotDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public Date getIssueAppearanceTime() {
        return issueAppearanceTime;
    }

    public void setIssueAppearanceTime(Date appearanceTime) {
        this.issueAppearanceTime = appearanceTime;
    }

    public Date getTicketCreationTime() {
        return ticketCreationTime;
    }

    public void setTicketCreationTime(Date issueCreatedTime) {
        this.ticketCreationTime = issueCreatedTime;
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

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reported) {
        this.reportedBy = reported;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
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

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public List<HistoryEvent> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryEvent> history) {
        this.history = history;
    }

    public static class Builder {
        private Issue newIssue;

        public Builder() {
            newIssue = new Issue();
        }

        public Builder withIssueId(String issueId) {
            newIssue.setIssueId(issueId);
            return this;
        }

        public Builder withShortDescription(String shortDescription) {
            newIssue.setShortDescription(shortDescription);
            return this;
        }

        public Builder withFullDescription(String fullDescription) {
            newIssue.setFullDescription(fullDescription);
            return this;
        }

        public Builder withReproduceSteps(String reproduceSteps) {
            newIssue.setReproduceSteps(reproduceSteps);
            return this;
        }

        public Builder withProductVersion(String productVersion) {
            newIssue.setProductVersion(productVersion);
            return this;
        }

        public Builder withReportedBy(User reportedBy) {
            newIssue.setReportedBy(reportedBy);
            return this;
        }

        public Builder withIssueAppearanceTime(Date issueAppearanceTime) {
            newIssue.setIssueAppearanceTime(issueAppearanceTime);
            return this;
        }

        public Builder withTicketCreationTime(Date ticketCreationTime) {
            newIssue.setTicketCreationTime(ticketCreationTime);
            return this;
        }

        public Issue build() {
            return newIssue;
        }
    }
}