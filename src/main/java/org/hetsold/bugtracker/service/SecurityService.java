package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface SecurityService extends UserDetailsService {

    void saveSecurityUser(SecurityUser securityUser);

    boolean isLoginTaken(String login);

    UserDTO getUserBySecurityUserLogin(String login);
}