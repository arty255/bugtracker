package org.hetsold.bugtracker.model;

/*
 * A Issue object describe basic data about issue
 */

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Issue extends AbstractIdentity {

    private String issueId;
    private String shotDescription;
    private String fullDescription;
    private Date issueAppearanceTime;
    private Date ticketCreationTime;
    private String productVersion;
    private String reproduceSteps;
    private String existedResult;
    private String expectedResult;
    @ManyToOne
    private User reportedBy;
    @ManyToOne
    private User assignedTo;
    private Severity severity;
    //todo: maybe optimize by using aggregator
    private String fixVersion;
    private State currentState;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private List<HistoryEvent> history;

    public Issue() {
        history = new ArrayList<>();
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getShotDescription() {
        return shotDescription;
    }

    public void setShotDescription(String shotDescription) {
        this.shotDescription = shotDescription;
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
}