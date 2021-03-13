package org.hetsold.bugtracker.service.exception;

public class EmptySecurityUserDataException extends RuntimeException {
    private static final long serialVersionUID = -1073511778749596406L;

    public EmptySecurityUserDataException(String filedName) {
        super("wrong securityUser data " + filedName);
    }
}
