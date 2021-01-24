package org.hetsold.bugtracker.model;

import java.util.Date;

public class IssueStateChangeEventDTO extends IssueEventType {
    private IssueState issueState;
    private UserDTO userDTO;
    private Date date;

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