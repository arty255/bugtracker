package org.hetsold.bugtracker.model.metadata;

import org.hetsold.bugtracker.model.IssueMessageEvent;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.IssueStateChangeEvent;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IssueMessageEvent.class)
public class IssueStateChangeEvent_ {
    public static volatile SingularAttribute<IssueStateChangeEvent, IssueState> issueState;
}
