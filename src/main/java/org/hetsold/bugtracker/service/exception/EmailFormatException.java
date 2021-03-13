package org.hetsold.bugtracker.service.exception;

public class EmailFormatException extends RuntimeException {
    private static final long serialVersionUID = -6871404815081562839L;

    public EmailFormatException() {
        super("wrong email");
    }
}
