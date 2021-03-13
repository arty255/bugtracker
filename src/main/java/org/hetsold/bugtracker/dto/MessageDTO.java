package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.Message;

import java.io.Serializable;
import java.util.Date;

public class MessageDTO extends IssueEventType implements Serializable {
    private static final long serialVersionUID = 2777213513516432249L;
    private String content;
    private Date createDate;
    private Date editDate;
    private UserDTO creator;
    private UserDTO editor;

    public MessageDTO() {
    }

    public MessageDTO(String uuid) {
        this.setUuid(uuid);
    }

    public MessageDTO(Message message) {
        this.setUuid(message.getUuid().toString());
        this.content = message.getContent();
        this.createDate = message.getCreateDate();
        this.editDate = message.getEditDate();
        this.creator = new UserDTO(message.getMessageCreator());
        if (message.getMessageEditor() != null) {
            this.editor = new UserDTO(message.getMessageEditor());
        }
    }

    public static MessageDTO createMessageDTOWithContent(String content) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setContent(content);
        return messageDTO;
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
}