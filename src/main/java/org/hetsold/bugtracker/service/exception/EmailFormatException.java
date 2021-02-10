package org.hetsold.bugtracker.service.exception;

public class EmailFormatException extends RuntimeException {
    public EmailFormatException() {
        super("wrong email");
    }
}
