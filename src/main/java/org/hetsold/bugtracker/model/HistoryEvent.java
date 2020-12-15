package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;

/*
 * This class describe issue events depending on state change
 * */

@Entity
@Table(name = "historyEvent")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HistoryEvent extends AbstractIdentity {
    @ManyToOne()
    @JoinTable(name = "issue_historyEvent", joinColumns = @JoinColumn(name = "historyId"), inverseJoinColumns = @JoinColumn(name = "issueId"))
    private Issue issue;
    private Date eventDate;

    public HistoryEvent() {
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}
