package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HistoryIssueStateChangeEvent extends HistoryEvent {
    private State state;
    private String expectedFixVersion;
    @OneToOne
    private User redactor;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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
