package org.hetsold.bugtracker.service;

import org.hetsold.bugtracker.dao.SecurityUserDAO;
import org.hetsold.bugtracker.dto.SecurityUserDTO;
import org.hetsold.bugtracker.dto.UserDTO;
import org.hetsold.bugtracker.facade.UserMapper;
import org.hetsold.bugtracker.model.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultSecurityService implements SecurityService {
    private PasswordEncoder encoder;
    private SecurityUserDAO securityUserDAO;

    @Autowired
    public void setEncoder(@Lazy PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public DefaultSecurityService(SecurityUserDAO securityUserDAO) {
        this.securityUserDAO = securityUserDAO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSecurityUser(SecurityUser securityUser) {
        if (encoder != null) {
            securityUser.setPassword(encoder.encode(securityUser.getPassword()));
        } else {
            securityUser.setPassword(NoOpPasswordEncoder.getInstance().encode(securityUser.getPassword()));
        }
        prepareSecurityUserData(securityUser);
        securityUserDAO.save(securityUser);
    }

    private void prepareSecurityUserData(SecurityUser securityUser) {
        securityUser.setAccountNonExpired(securityUser.isEnabled());
        securityUser.setAccountNonLocked(securityUser.isEnabled());
        securityUser.setCredentialsNonExpired(securityUser.isEnabled());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isLoginTaken(String login) {
        return securityUserDAO.isLoginTaken(login);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserDTO getUserBySecurityUserLogin(String login) {
        return UserMapper.getUserDTO(securityUserDAO.getUserBySecurityUserLogin(login));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SecurityUserDTO loadUserByUsername(String login) throws UsernameNotFoundException {
        SecurityUser securityUser = securityUserDAO.getSecUserByUsername(login);
        if (securityUser != null) {
            return new SecurityUserDTO(securityUser);
        } else {
            throw new UsernameNotFoundException("login error");
        }
    }
}