package org.hetsold.bugtracker.model;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "message")
public class Message extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageCreator")
    private User messageCreator;
    private String content;
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "messageEditor")
    private User messageEditor;
    private Date editDate;
    private Boolean archived;

    protected Message() {
    }

    @PrePersist
    public void prePersist() {
        this.createDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.editDate = new Date();
    }

    public void updateContentAndEditor(Message message, User messageEditor) {
        this.content = message.getContent();
        this.messageEditor = messageEditor;
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

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public static class Builder {
        private final Message message;

        public Builder() {
            this.message = new Message();
        }

        public Builder withUUID(UUID uuid) {
            message.setUuid(uuid);
            return this;
        }

        public Builder withCreator(User creator) {
            message.setMessageCreator(creator);
            return this;
        }

        public Builder withContent(String content) {
            message.setContent(content);
            return this;
        }

        public Message build() {
            return message;
        }
    }
}