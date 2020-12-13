package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Issue_ {
    public static volatile SingularAttribute<Issue, String> uuid;
    public static volatile SingularAttribute<Issue, Date> ticketCreationTime;
    public static volatile SingularAttribute<Issue, Date> issueAppearanceTime;
    public static volatile SingularAttribute<Issue, User> reportedBy;
    public static volatile SingularAttribute<Issue, State> currentState;
    public static volatile SingularAttribute<Issue, String> shortDescription;
    public static volatile SingularAttribute<Issue, String> fullDescription;
}
