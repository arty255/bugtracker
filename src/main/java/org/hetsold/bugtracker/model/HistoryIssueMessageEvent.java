package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BT_ISSUEMESSAGEEVENT")
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