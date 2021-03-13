package org.hetsold.bugtracker.service.exception;

public class EmptyUUIDKeyException extends RuntimeException {
    private static final long serialVersionUID = 3648671634752169736L;

    public EmptyUUIDKeyException(String message) {
        super(message);
    }

    public EmptyUUIDKeyException() {
        super("Wrong id key");
    }
}
