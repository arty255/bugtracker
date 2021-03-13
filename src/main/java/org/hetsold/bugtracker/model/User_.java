package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;

public class User_ extends ArchivedEntity_ {
    public static volatile SingularAttribute<User, String> firstName;
    public static volatile SingularAttribute<User, String> lastName;
    public static volatile SingularAttribute<User, Date> registrationDate;
}
