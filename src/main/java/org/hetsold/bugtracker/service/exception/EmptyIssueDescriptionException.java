package org.hetsold.bugtracker.service.exception;

public class EmptyIssueDescriptionException extends RuntimeException {
    public EmptyIssueDescriptionException() {
        super("message description can not be empty");
    }
}
