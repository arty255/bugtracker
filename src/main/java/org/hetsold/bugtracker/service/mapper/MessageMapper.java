package org.hetsold.bugtracker.service.mapper;

import org.hetsold.bugtracker.dto.MessageDTO;
import org.hetsold.bugtracker.model.Message;

import java.util.List;
import java.util.stream.Collectors;

public final class MessageMapper {

    private MessageMapper() {
    }

    public static MessageDTO getMessageDTO(Message message) {
        return new MessageDTO(message);
    }

    public static Message getMessage(MessageDTO messageDTO) {
        if (messageDTO != null) {
            Message message = new Message.Builder()
                    .withUUID(UUIDMapper.getUUID(messageDTO))
                    .withContent(messageDTO.getContent())
                    .withCreator(UserMapper.getUser(messageDTO.getCreator()))
                    .build();
            message.setMessageEditor(UserMapper.getUser(messageDTO.getEditor()));
            message.setCreateDate(messageDTO.getCreateDate());
            message.setEditDate(messageDTO.getEditDate());
            if (messageDTO.getCreator() != null) {
                message.setMessageCreator(UserMapper.getUser(messageDTO.getCreator()));
            }
            return message;
        }
        return null;
    }

    public static List<MessageDTO> getMessageDTOList(List<Message> messages) {
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
