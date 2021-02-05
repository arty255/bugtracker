package org.hetsold.bugtracker.model;

import javax.persistence.metamodel.SingularAttribute;

public class SecurityUser_ extends AbstractEntity_ {
    public static volatile SingularAttribute<SecurityUser, String> username;
    public static volatile SingularAttribute<SecurityUser, String> password;
    public static volatile SingularAttribute<SecurityUser, User> user;
}
