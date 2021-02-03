package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

@StaticMetamodel(Message.class)
public class Message_ extends ArchivedEntity_ {
    public static volatile SingularAttribute<Message, Date> createDate;

    public static String UUID_NAME = "uuid";
    public static String CREATE_DATE_NAME = "createDate";
}
