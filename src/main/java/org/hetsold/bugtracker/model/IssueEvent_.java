package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class IssueEvent_ extends AbstractEntity_ {
    public static volatile SingularAttribute<IssueEvent, Issue> issue;
    public static volatile SingularAttribute<IssueEvent, Date> eventDate;
}
