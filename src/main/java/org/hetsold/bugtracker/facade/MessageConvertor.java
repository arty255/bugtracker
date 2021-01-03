package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.model.MessageDTO;

public class MessageConvertor {

    public static MessageDTO getMessageDTO(Message message) {
        return new MessageDTO(message);
    }

    public static Message getMessage(MessageDTO messageDTO) {
        Message message = null;
        if (messageDTO != null) {
            message = new Message();
            message.setUuid(messageDTO.getUuid());
            message.setContent(messageDTO.getContent());
            message.setMessageEditor(UserConvertor.getUser(messageDTO.getEditor()));
            message.setMessageCreator(UserConvertor.getUser(messageDTO.getCreator()));
            message.setCreateDate(messageDTO.getCreateDate());
            message.setEditDate(messageDTO.getEditDate());
        }
        return message;
    }
}
