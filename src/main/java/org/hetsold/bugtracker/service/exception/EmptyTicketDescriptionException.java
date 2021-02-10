package org.hetsold.bugtracker.service.exception;

public class EmptyTicketDescriptionException extends RuntimeException {
    public EmptyTicketDescriptionException() {
        super("ticket description can not be empty");
    }
}
