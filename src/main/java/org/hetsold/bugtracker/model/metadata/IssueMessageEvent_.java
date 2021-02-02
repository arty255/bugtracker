package org.hetsold.bugtracker.model.metadata;

import org.hetsold.bugtracker.model.IssueMessageEvent;
import org.hetsold.bugtracker.model.Message;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IssueMessageEvent.class)
public class IssueMessageEvent_ {
    public static volatile SingularAttribute<IssueMessageEvent, String> uuid;
    public static volatile SingularAttribute<IssueMessageEvent, Message> message;
}
