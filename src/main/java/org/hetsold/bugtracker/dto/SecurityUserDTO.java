package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUserAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUserDTO extends AbstractDTO implements UserDetails {
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private Collection<SecurityUserAuthority> authorities = new ArrayList<>();
    private UserDTO userDTO;

    public SecurityUserDTO() {
    }

    public SecurityUserDTO(SecurityUser securityUser) {
        this.setUuid(securityUser.getUuid().toString());
        this.username = securityUser.getUsername();
        this.enabled = securityUser.isEnabled();
        this.accountNonExpired = securityUser.isAccountNonExpired();
        this.accountNonLocked = securityUser.isAccountNonLocked();
        this.credentialsNonExpired = securityUser.isCredentialsNonExpired();
        this.authorities.addAll(securityUser.getAuthorities());
        if (securityUser.getUser() != null) {
            this.userDTO = new UserDTO(securityUser.getUser());
        }
    }

    @Override
    public Collection<SecurityUserAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAuthorities(Collection<SecurityUserAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}