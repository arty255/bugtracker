package org.hetsold.bugtracker.model.metadata;

import org.hetsold.bugtracker.model.*;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Issue.class)
public class Issue_ {
    public static volatile SingularAttribute<Issue, String> uuid;
    public static volatile SingularAttribute<Issue, Date> creationTime;
    public static volatile SingularAttribute<Issue, User> reportedBy;
    public static volatile SingularAttribute<Issue, IssueState> currentIssueState;
    public static volatile SingularAttribute<Issue, String> description;
    public static volatile ListAttribute<Issue, IssueEvent> history;
    public static volatile SingularAttribute<Issue, Severity> severity;
    public static volatile SingularAttribute<Issue, Boolean> archived;
}
