package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.dto.user.UserDTO;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.IssueStateChangeEvent;

import java.io.Serializable;
import java.util.Date;

public class IssueStateChangeEventDTO extends IssueEventType implements Serializable {
    private static final long serialVersionUID = 2966624598411126549L;
    private final IssueState issueState;
    private final UserDTO userDTO;
    private final Date date;

    public IssueStateChangeEventDTO(IssueStateChangeEvent stateChangeEvent) {
        this.userDTO = new UserDTO(stateChangeEvent.getRedactor());
        this.issueState = stateChangeEvent.getState();
        this.date = stateChangeEvent.getEventDate();
    }

    public IssueState getIssueState() {
        return issueState;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public Date getDate() {
        return date;
    }
}
