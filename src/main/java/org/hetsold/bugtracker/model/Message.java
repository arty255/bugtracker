package org.hetsold.bugtracker.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Message extends AbstractIdentity {
    @ManyToOne
    private User messageCreator;
    private String title;
    private String messageContent;

    public Message() {
    }

    public User getMessageCreator() {
        return messageCreator;
    }

    public void setMessageCreator(User messageCreator) {
        this.messageCreator = messageCreator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
