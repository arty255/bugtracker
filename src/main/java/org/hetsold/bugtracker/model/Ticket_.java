package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Ticket_ extends ArchivedEntity_ {
    public static volatile SingularAttribute<Ticket, String> description;
    public static volatile SingularAttribute<Ticket, String> reproduceSteps;
    public static volatile SingularAttribute<Ticket, String> productVersion;
    public static volatile SingularAttribute<Ticket, Date> creationTime;
    public static volatile SingularAttribute<Ticket, User> createdBy;
    public static volatile SingularAttribute<Ticket, TicketVerificationState> verificationState;
    public static volatile SingularAttribute<Ticket, TicketResolveState> resolveState;
    public static volatile ListAttribute<Ticket, Message> messageList;

    public static String UUID_NAME = "uuid";
    public static String MESSAGE_LIST_NAME = "messageList";
    public static String CREATED_BY_NAME = "createdBy";
}