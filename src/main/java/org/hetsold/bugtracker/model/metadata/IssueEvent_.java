package org.hetsold.bugtracker.model.metadata;

import org.hetsold.bugtracker.model.Issue;
import org.hetsold.bugtracker.model.IssueEvent;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(IssueEvent_.class)
public class IssueEvent_ {
    public static volatile SingularAttribute<IssueEvent, Issue> issue;
    public static volatile SingularAttribute<IssueEvent, Date> eventDate;
}