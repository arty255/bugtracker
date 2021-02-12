package org.hetsold.bugtracker.service.exception;

public class TakenLoginException extends IllegalArgumentException {
    public TakenLoginException(String login) {
        super("login " + login + " already persisted");
    }
}