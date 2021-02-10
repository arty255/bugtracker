package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.*;
import org.hetsold.bugtracker.service.exception.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {
    static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private ValidationHelper() {
    }

    public static void validateSecurityUserAndUUID(SecurityUser securityUser) {
        validateNotNull(securityUser, "securityUser can not be null");
        validateNotNullUUID(securityUser.getUuid());
    }

    public static void validateUserAndUUID(User user) {
        validateNotNull(user, "user can not be null");
        validateNotNullUUID(user.getUuid());
    }

    public static void validateTicketAndUUID(Ticket ticket) {
        validateNotNull(ticket, "ticket can not be null");
        validateNotNullUUID(ticket.getUuid());
    }

    public static void validateMessageAndUUID(Message message) {
        validateNotNull(message, "message can not be null");
        validateNotNullUUID(message.getUuid());
    }

    public static void validateIssueAndUUID(Issue issue) {
        validateNotNull(issue, "issue can not be null");
        validateNotNullUUID(issue.getUuid());
    }

    public static void validateUserBeforeSave(User user) {
        validateNotNull(user, "user can not be null");
        validateNull(user.getUuid(), "user idd need to be null");
        validateUserFirstName(user);
        validateUserLastName(user);
    }

    public static void validateSecurityUserBeforeSave(SecurityUser securityUser) {
        validateNotNull(securityUser, "securityUser can not be null");
        validateNull(securityUser.getUuid(), "securityUser id need to be null");
        if (securityUser.getUsername() == null || securityUser.getUsername().length() < 3) {
            throw new EmptySecurityUserDataException("username length is incorrect");
        }
        validatePassword(securityUser.getPassword());
    }

    public static void validateTicketBeforeSave(Ticket ticket) {
        validateNotNull(ticket, "ticket can not be empty");
        validateNull(ticket.getUuid(), "ticket uuid need to ne null");
        validateTicketDescription(ticket);
        validateUserAndUUID(ticket.getCreatedBy());
    }

    public static void validateMessageBeforeSave(Message message) {
        validateNotNull(message, "message can not be null");
        validateNull(message.getUuid(), "message uuid need to be null");
        validateMessageContent(message);
        validateUserAndUUID(message.getMessageCreator());
    }

    public static void validateIssueBeforeSave(Issue issue) {
        validateNotNull(issue, "issue can not be null");
        validateNull(issue.getUuid(), "issue id can not be not null");
        validateDescription(issue);
    }

    public static void validateTicketBeforeUpdate(Ticket ticket) {
        validateTicketAndUUID(ticket);
        validateTicketDescription(ticket);
        validateUserAndUUID(ticket.getCreatedBy());
    }

    public static void validateTicketDescription(Ticket ticket) {
        validateNotNull(ticket, "ticket can not be empty");
        if (ticket.getDescription() == null || ticket.getDescription().isEmpty()) {
            throw new EmptyTicketDescriptionException();
        }
    }

    public static void validateUserFirstName(User user) {
        if (user.getFirstName() == null || user.getFirstName().length() < 3) {
            throw new ContentMismatchException("lastName", "cannot be null");
        }
    }

    public static void validateUserLastName(User user) {
        if (user.getLastName() == null || user.getLastName().length() < 3) {
            throw new ContentMismatchException("lastName", "cannot be null");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new EmptySecurityUserDataException("password length is incorrect");
        }
    }

    public static void validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        if (!matcher.find()) {
            throw new EmailFormatException();
        }
    }

    public static void validateMessageContent(Message message) {
        validateNotNull(message, "message can not be null");
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new EmptyMessageContentException();
        }
    }

    public static void validateDescription(Issue issue) {
        validateNotNull(issue, "issue can not be null");
        if (issue.getDescription() == null || issue.getDescription().isEmpty()) {
            throw new EmptyIssueDescriptionException();
        }
    }

    public static void validateIssueState(IssueState issueState) {
        if (issueState == null) {
            throw new IllegalArgumentException("new issue state can not be null");
        }
    }

    public static void validateNotNullUUID(UUID uuid) {
        if (uuid == null) {
            throw new EmptyUUIDKeyException();
        }
    }

    public static void validateNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateNull(Object o, String message) {
        if (o != null) {
            throw new IllegalArgumentException(message);
        }
    }
}