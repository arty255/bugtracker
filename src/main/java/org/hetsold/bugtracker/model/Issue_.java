package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Issue_ {
    public static volatile SingularAttribute<Issue, String> uuid;
    public static volatile SingularAttribute<Issue, Date> creationTime;
    public static volatile SingularAttribute<Issue, User> reportedBy;
    public static volatile SingularAttribute<Issue, State> currentState;
    public static volatile SingularAttribute<Issue, String> description;
    public static volatile ListAttribute<Issue, HistoryEvent> history;
}
