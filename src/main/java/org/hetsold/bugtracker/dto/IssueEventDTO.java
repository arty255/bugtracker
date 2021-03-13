package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.model.IssueEvent;
import org.hetsold.bugtracker.model.IssueMessageEvent;
import org.hetsold.bugtracker.model.IssueStateChangeEvent;

import java.io.Serializable;

public class IssueEventDTO implements Serializable {
    private static final long serialVersionUID = 2933517846660006549L;
    private final EventType eventType;
    private final IssueEventType entityDTO;

    public enum EventType {
        MESSAGE_EVENT, STATE_CHANGE_EVENT
    }

    public IssueEventDTO(EventType eventType, IssueEventType entityDTO) {
        this.eventType = eventType;
        this.entityDTO = entityDTO;
    }

    public IssueEventDTO(IssueEvent issueEvent) {
        if (issueEvent instanceof IssueMessageEvent) {
            this.eventType = EventType.MESSAGE_EVENT;
            entityDTO = new MessageDTO(((IssueMessageEvent) issueEvent).getMessage());
        } else {
            this.eventType = EventType.STATE_CHANGE_EVENT;
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