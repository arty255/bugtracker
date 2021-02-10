package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;

import java.util.UUID;

public interface SecurityUserDAO {
    boolean isLoginTaken(String login);

    SecurityUser getSecUserByUsername(String username);

    void save(SecurityUser securityUser);

    User getUserBySecurityUserLogin(String login);

    SecurityUser getSecurityUserByUuid(UUID uuid);

    SecurityUser getSecurityUserByUserUuid(UUID uuid);

    void delete(SecurityUser securityUser);
}
