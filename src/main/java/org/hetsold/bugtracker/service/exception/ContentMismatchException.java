package org.hetsold.bugtracker.service.exception;

public class ContentMismatchException extends RuntimeException {
    public ContentMismatchException(String fieldName, String reason) {
        super("wrong " + fieldName + " data : " + reason);
    }
}
