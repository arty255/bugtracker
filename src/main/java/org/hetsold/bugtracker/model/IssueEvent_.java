package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(IssueEvent_.class)
public class IssueEvent_ extends AbstractEntity_ {
    public static volatile SingularAttribute<IssueEvent, Issue> issue;
    public static volatile SingularAttribute<IssueEvent, Date> eventDate;
    public static String ISSUE_NAME = "issue";
    public static String EVENT_DATE_NAME = "eventDate";
}
