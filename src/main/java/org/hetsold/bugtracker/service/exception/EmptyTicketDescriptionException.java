package org.hetsold.bugtracker.service.exception;

public class EmptyTicketDescriptionException extends RuntimeException {
    private static final long serialVersionUID = 8013314011947401358L;

    public EmptyTicketDescriptionException() {
        super("ticket description can not be empty");
    }
}
