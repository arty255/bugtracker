package org.hetsold.bugtracker.service.exception;

public class TakenLoginException extends IllegalArgumentException {
    private static final long serialVersionUID = 6792920337255590859L;

    public TakenLoginException(String login) {
        super("login " + login + " already persisted");
    }
}