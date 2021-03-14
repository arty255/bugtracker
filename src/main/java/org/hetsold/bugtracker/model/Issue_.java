package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Issue_ extends AbstractEntity_ {
    public static volatile SingularAttribute<Issue, Date> creationTime;
    public static volatile SingularAttribute<Issue, User> reportedBy;
    public static volatile SingularAttribute<Issue, IssueState> currentIssueState;
    public static volatile SingularAttribute<Issue, String> description;
    public static volatile ListAttribute<Issue, IssueEvent> history;
    public static volatile SingularAttribute<Issue, Severity> severity;
    public static volatile SingularAttribute<Issue, Boolean> archived;
}