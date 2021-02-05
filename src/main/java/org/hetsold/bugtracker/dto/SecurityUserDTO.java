package org.hetsold.bugtracker.dto;

import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUserAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class SecurityUserDTO implements UserDetails {
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private Collection<SecurityUserAuthority> authorities = new ArrayList<>();
    private UserDTO userDTO;

    public SecurityUserDTO(SecurityUser securityUser) {
        this.username = securityUser.getUsername();
        this.password = securityUser.getPassword();
        this.enabled = securityUser.isEnabled();
        this.accountNonExpired = securityUser.isAccountNonExpired();
        this.accountNonLocked = securityUser.isAccountNonLocked();
        this.credentialsNonExpired = securityUser.isCredentialsNonExpired();
        this.authorities.addAll(securityUser.getAuthorities());
        this.userDTO = new UserDTO(securityUser.getUser());
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
}