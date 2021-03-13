package org.hetsold.bugtracker.service.exception;

public class EmptyMessageContentException extends RuntimeException {
    private static final long serialVersionUID = -2081771840844212752L;

    public EmptyMessageContentException() {
        super("message content can not be empty");
    }
}