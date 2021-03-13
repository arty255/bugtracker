package org.hetsold.bugtracker.service.exception;

public class MessageUserIntegrityException extends RuntimeException {
    private static final long serialVersionUID = -4442199607018107966L;

    public MessageUserIntegrityException() {
        super("message creator can not be null");
    }
}
