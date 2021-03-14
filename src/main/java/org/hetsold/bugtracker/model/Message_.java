package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class Message_ extends AbstractEntity_ {
    public static volatile SingularAttribute<Message, Date> createDate;
    public static volatile SingularAttribute<Message, Boolean> archived;
}
