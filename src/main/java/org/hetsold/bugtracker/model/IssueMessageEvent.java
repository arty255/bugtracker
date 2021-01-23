package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "issueMessageEvent")
public class IssueMessageEvent extends IssueEvent {
    @OneToOne
    @JoinColumn(name = "messageId")
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}