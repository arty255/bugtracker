package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IssueMessageEvent.class)
public class IssueStateChangeEvent_ {
    public static volatile SingularAttribute<IssueStateChangeEvent, IssueState> issueState;
}
