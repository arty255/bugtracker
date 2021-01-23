package org.hetsold.bugtracker.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class MessageDTO extends IssueEventType implements Serializable {
    private String uuid;
    private String content;
    private Date createDate;
    private Date editDate;
    private UserDTO creator;
    private UserDTO editor;

    public MessageDTO() {
    }

    public MessageDTO(String content) {
        this.content = content;
    }

    public MessageDTO(Message message) {
        this.uuid = message.getUuid();
        this.content = message.getContent();
        this.createDate = message.getCreateDate();
        this.editDate = message.getEditDate();
        this.creator = new UserDTO(message.getMessageCreator());
        if (message.getMessageEditor() != null) {
            this.editor = new UserDTO(message.getMessageEditor());
        }
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public UserDTO getEditor() {
        return editor;
    }

    public void setEditor(UserDTO editor) {
        this.editor = editor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}