package org.hetsold.bugtracker.view;


import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.dto.user.UserDTO;

import java.io.Serializable;
import java.util.Date;

public abstract class ListableMessageBean implements Serializable {
    private boolean editMode;
    private MessageDTO selectedToEditMessage;
    private MessageDTO selectedToDeleteMessage;
    protected UserDTO activeUser;


    public void initMessageListener() {
        selectedToEditMessage = new MessageDTO();
    }

    public abstract void preformUpdateOperation(MessageDTO messageDTO);

    public abstract void preformSaveOperation(MessageDTO messageDTO);

    public abstract void preformDeleteOperation(MessageDTO messageDTO);

    public void editMessageAction() {
        preformUpdateOperation(selectedToEditMessage);
        initMessageListener();
        editMode = false;
    }

    public void saveMessageAction() {
        preformSaveOperation(selectedToEditMessage);
        initMessageListener();
        editMode = false;
    }

    public void deleteMessageAction() {
        preformDeleteOperation(selectedToDeleteMessage);
        if (selectedToEditMessage == selectedToDeleteMessage) {
            cancelEditAction();
        }
    }

    public void editMessagePrepareAction() {
        editMode = true;
    }

    public void cancelEditAction() {
        editMode = false;
        initMessageListener();
    }

    public void initPreviewListener() {
        if (editMode) {
            selectedToEditMessage.setEditor(activeUser);
            selectedToEditMessage.setEditDate(new Date());
        } else {
            selectedToEditMessage.setCreator(activeUser);
            selectedToEditMessage.setCreateDate(new Date());
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public MessageDTO getSelectedToEditMessage() {
        return selectedToEditMessage;
    }

    public void setSelectedToEditMessage(MessageDTO selectedToEditMessage) {
        this.selectedToEditMessage = selectedToEditMessage;
    }

    public MessageDTO getSelectedToDeleteMessage() {
        return selectedToDeleteMessage;
    }

    public void setSelectedToDeleteMessage(MessageDTO selectedToDeleteMessage) {
        this.selectedToDeleteMessage = selectedToDeleteMessage;
    }
}