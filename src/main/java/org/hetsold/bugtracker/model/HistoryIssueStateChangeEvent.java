package org.hetsold.bugtracker.model;

import javax.persistence.*;

@Entity
@Table(name = "issueStateChangeEvent")
public class HistoryIssueStateChangeEvent extends HistoryEvent {
    @Enumerated
    private IssueState issueState;
    private String expectedFixVersion;
    @OneToOne
    @JoinColumn(name = "redactorId")
    private User redactor;

    public IssueState getState() {
        return issueState;
    }

    public void setState(IssueState issueState) {
        this.issueState = issueState;
    }

    public String getExpectedFixVersion() {
        return expectedFixVersion;
    }

    public void setExpectedFixVersion(String expectedFixVersion) {
        this.expectedFixVersion = expectedFixVersion;
    }

    public User getRedactor() {
        return redactor;
    }

    public void setRedactor(User redactor) {
        this.redactor = redactor;
    }
}
