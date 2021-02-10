package org.hetsold.bugtracker.service.exception;

public class EmptyUUIDKeyException extends RuntimeException {
    public EmptyUUIDKeyException(String message) {
        super(message);
    }

    public EmptyUUIDKeyException() {
        super("Wrong id key");
    }
}
