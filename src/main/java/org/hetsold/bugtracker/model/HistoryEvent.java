package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;

/*
 * This class describe issue events depending on state change
 * */

@Entity
@Table(name = "BT_HISTORYEVENT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class HistoryEvent extends AbstractIdentity {
    @ManyToOne
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
