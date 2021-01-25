package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(IssueMessageEvent.class)
public class IssueMessageEvent_ {
    public static volatile SingularAttribute<IssueMessageEvent, String> uuid;
    public static volatile SingularAttribute<IssueMessageEvent, Message> message;
}
