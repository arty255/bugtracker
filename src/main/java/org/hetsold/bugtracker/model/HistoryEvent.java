package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * This class describe issue events depending on state change
 * */

@Entity
public class HistoryEvent extends AbstractIdentity {
    @ManyToOne
    private Issue issue;
    private State state;
    private Date eventDate;
    private String expectedFixVersion;
    @OneToMany
    private List<Message> relatedMessageList;

    public HistoryEvent() {
        relatedMessageList = new ArrayList<>();
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public List<Message> getRelatedMessageList() {
        return relatedMessageList;
    }

    public void setRelatedMessageList(List<Message> relatedMessageList) {
        this.relatedMessageList = relatedMessageList;
    }

    public String getExpectedFixVersion() {
        return expectedFixVersion;
    }

    public void setExpectedFixVersion(String expectedFixVersion) {
        this.expectedFixVersion = expectedFixVersion;
    }
}
