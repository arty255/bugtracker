package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.model.IssueEvent;
import org.hetsold.bugtracker.model.IssueMessageEvent;
import org.hetsold.bugtracker.model.IssueStateChangeEvent;

public class IssueEventDTO {
    private EventType eventType;
    private IssueEventType entityDTO;

    public enum EventType {
        MessageEvent, StateChangeEvent
    }

    public IssueEventDTO(EventType eventType, IssueEventType entityDTO) {
        this.eventType = eventType;
        this.entityDTO = entityDTO;
    }

    public IssueEventDTO(IssueEvent issueEvent) {
        if (issueEvent instanceof IssueMessageEvent) {
            this.eventType = EventType.MessageEvent;
            entityDTO = new MessageDTO(((IssueMessageEvent) issueEvent).getMessage());
        } else {
            this.eventType = EventType.StateChangeEvent;
            entityDTO = new IssueStateChangeEventDTO(((IssueStateChangeEvent) issueEvent));
        }
    }

    public EventType getEventType() {
        return eventType;
    }

    public IssueEventType getEntityDTO() {
        return entityDTO;
    }
}