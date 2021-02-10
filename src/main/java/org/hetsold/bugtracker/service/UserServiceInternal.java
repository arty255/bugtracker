package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;

public interface UserServiceInternal {

    void registerUser(User user, SecurityUser securityUser);

    User updateUser(User user);

    User getUser(User user);

    void deleteUser(User user);

    SecurityUser getSecurityUser(SecurityUser securityUser);

    void deleteSecurityUser(SecurityUser securityUser);
}