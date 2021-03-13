package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "issueEvent")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class IssueEvent extends AbstractEntity {
    @ManyToOne()
    @JoinTable(name = "issue_issueEvent", joinColumns = @JoinColumn(name = "issueEventId"), inverseJoinColumns = @JoinColumn(name = "issueId"))
    private Issue issue;
    private Date eventDate;

    protected IssueEvent() {
    }

    @PrePersist
    public void prePersist() {
        this.eventDate = new Date();
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
