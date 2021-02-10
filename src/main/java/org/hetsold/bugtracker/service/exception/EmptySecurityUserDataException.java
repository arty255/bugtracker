package org.hetsold.bugtracker.service.exception;

public class EmptySecurityUserDataException extends RuntimeException {
    public EmptySecurityUserDataException(String filedName) {
        super("wrong securityUser data " + filedName);
    }
}
