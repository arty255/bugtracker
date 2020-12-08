package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HistoryIssueMessageEvent extends HistoryEvent {
    @OneToOne
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}