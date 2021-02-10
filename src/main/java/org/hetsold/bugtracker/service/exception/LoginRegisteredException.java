package org.hetsold.bugtracker.service.exception;

public class LoginRegisteredException extends IllegalArgumentException {
    public LoginRegisteredException(String login) {
        super("login " + login + " already persisted");
    }
}