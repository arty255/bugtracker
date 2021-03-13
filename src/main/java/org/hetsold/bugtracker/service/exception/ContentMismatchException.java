package org.hetsold.bugtracker.service.exception;

public class ContentMismatchException extends RuntimeException {
    private static final long serialVersionUID = -3135453870841561049L;

    public ContentMismatchException(String fieldName, String reason) {
        super("wrong " + fieldName + " data : " + reason);
    }
}
