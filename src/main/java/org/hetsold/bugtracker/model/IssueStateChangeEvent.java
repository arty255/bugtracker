package org.hetsold.bugtracker.model;

import javax.persistence.*;

@Entity
@Table(name = "issueStateChangeEvent")
public class IssueStateChangeEvent extends IssueEvent {
    @Enumerated
    private IssueState issueState;
    @OneToOne
    @JoinColumn(name = "redactorId")
    private User redactor;

    public IssueState getState() {
        return issueState;
    }

    public void setState(IssueState issueState) {
        this.issueState = issueState;
    }

    public User getRedactor() {
        return redactor;
    }

    public void setRedactor(User redactor) {
        this.redactor = redactor;
    }
}
