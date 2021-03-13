package org.hetsold.bugtracker.service.exception;

public class EmptyIssueDescriptionException extends RuntimeException {
    private static final long serialVersionUID = 8960290578045061741L;

    public EmptyIssueDescriptionException() {
        super("message description can not be empty");
    }
}
