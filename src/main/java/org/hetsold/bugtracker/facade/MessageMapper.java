package org.hetsold.bugtracker.facade;

import org.hetsold.bugtracker.model.Message;
import org.hetsold.bugtracker.dto.MessageDTO;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {

    public static MessageDTO getMessageDTO(Message message) {
        return new MessageDTO(message);
    }

    public static Message getMessage(MessageDTO messageDTO) {
        Message message = null;
        if (messageDTO != null) {
            message = new Message();
            message.setUuid(messageDTO.getUuid());
            message.setContent(messageDTO.getContent());
            message.setMessageEditor(UserMapper.getUser(messageDTO.getEditor()));
            message.setMessageCreator(UserMapper.getUser(messageDTO.getCreator()));
            message.setCreateDate(messageDTO.getCreateDate());
            message.setEditDate(messageDTO.getEditDate());
        }
        return message;
    }

    public static List<MessageDTO> getMessageDTOList(List<Message> messages) {
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
