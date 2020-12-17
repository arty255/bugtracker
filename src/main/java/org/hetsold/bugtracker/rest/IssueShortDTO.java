package org.hetsold.bugtracker.rest;

import org.hetsold.bugtracker.model.State;

import java.io.Serializable;
import java.util.Date;

public class IssueShortDTO implements Serializable {
    private String uuid;
    private String issueId;
    private String description;
    private Date dateOfCreation;
    private State state;

    public IssueShortDTO() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
