package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Message.class)
public class Message_ {
    public static volatile SingularAttribute<Message, String> uuid;
    public static volatile SingularAttribute<Message, Date> createDate;
}
