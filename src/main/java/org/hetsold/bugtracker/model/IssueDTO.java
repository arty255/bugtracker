package org.hetsold.bugtracker.model;

import java.util.Date;

public class IssueDTO {
    private String uuid;
    private String issueNumber;
    private String description;
    private Date creationTime;
    private String productVersion;
    private String reproduceSteps;
    private String existedResult;
    private String expectedResult;
    private Severity severity;
    private String fixVersion;
    private State currentState;

    public IssueDTO(Issue issue) {
        this.uuid = issue.getUuid();
        this.issueNumber = issue.getIssueNumber();
        this.description = issue.getDescription();
        this.creationTime = issue.getCreationTime();
        this.productVersion = issue.getProductVersion();
        this.reproduceSteps = issue.getReproduceSteps();
        this.existedResult = issue.getExistedResult();
        this.expectedResult = issue.getExpectedResult();
        this.severity = issue.getSeverity();
        this.fixVersion = issue.getFixVersion();
        this.currentState = issue.getCurrentState();
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

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
