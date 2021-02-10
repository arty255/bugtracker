package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IssueMessageEvent.class)
public class IssueStateChangeEvent_ extends IssueEvent_ {
    public static volatile SingularAttribute<IssueStateChangeEvent, IssueState> issueState;
    public static String ISSUE_STATE_NAME = "issueState";
}
