package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message extends AbstractIdentity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageCreator")
    private User messageCreator;
    private String title;
    private String content;
    private Date messageDate;

    public Message() {
    }

    public Message(User messageCreator, String title, String messageContent) {
        this.messageCreator = messageCreator;
        this.title = title;
        this.content = messageContent;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date date) {
        this.messageDate = date;
    }
}
