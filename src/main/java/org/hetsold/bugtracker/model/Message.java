package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message extends AbstractIdentity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageCreator")
    private User messageCreator;
    private String content;
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageEditor")
    private User messageEditor;
    private Date editDate;

    public Message() {
    }

    public Message(User messageCreator, String messageContent) {
        this.messageCreator = messageCreator;
        this.content = messageContent;
    }

    public User getMessageCreator() {
        return messageCreator;
    }

    public void setMessageCreator(User messageCreator) {
        this.messageCreator = messageCreator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date date) {
        this.createDate = date;
    }

    public User getMessageEditor() {
        return messageEditor;
    }

    public void setMessageEditor(User messageEditor) {
        this.messageEditor = messageEditor;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}
