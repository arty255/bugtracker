package org.hetsold.bugtracker.service.exception;

public class MessageUserIntegrityException extends RuntimeException {
    public MessageUserIntegrityException() {
        super("message creator can not be null");
    }
}
