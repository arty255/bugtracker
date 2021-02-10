package org.hetsold.bugtracker.service.exception;

public class EmptyMessageContentException extends RuntimeException {
    public EmptyMessageContentException() {
        super("message content can not be empty");
    }
}