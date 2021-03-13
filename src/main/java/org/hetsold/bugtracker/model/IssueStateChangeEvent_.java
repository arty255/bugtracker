package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;

public class IssueStateChangeEvent_ extends IssueEvent_ {
    public static volatile SingularAttribute<IssueStateChangeEvent, IssueState> issueState;
}
