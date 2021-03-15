package org.hetsold.bugtracker.model;

import org.hetsold.bugtracker.dao.util.BooleanToStringConverter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@Entity
@Table(name = "securityUser")
public class SecurityUser extends AbstractEntity implements UserDetails {
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean enabled;
    @Column(name = "accountNonExpired")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean accountNonExpired;
    @Column(name = "accountNonLocked")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean accountNonLocked;
    @Column(name = "credentialsNonExpired")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean credentialsNonExpired;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "securityUser_authority", joinColumns = @JoinColumn(name = "secUserId", referencedColumnName = "uuid"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Collection<SecurityUserAuthority> authorities;
    @OneToOne()
    @JoinColumn(name = "userId")
    private User user;
    private String email;

    protected SecurityUser() {
        authorities = new HashSet<>();
    }

    public void updateNotSensitiveData(SecurityUser newSecurityUser) {
        this.setEnabled(newSecurityUser.isEnabled());
        this.setAccountNonExpired(newSecurityUser.isAccountNonExpired());
        this.setAccountNonLocked(newSecurityUser.isAccountNonLocked());
        this.setCredentialsNonExpired(newSecurityUser.isCredentialsNonExpired());
        this.setAuthorities(newSecurityUser.getAuthorities());
        this.setEmail(newSecurityUser.getEmail());
        this.setUsername(newSecurityUser.getUsername());
    }

    public void fullUpdate(SecurityUser securityUser) {
        updateNotSensitiveData(securityUser);
        this.setPassword(securityUser.getPassword());
    }

    @Override
    public Collection<SecurityUserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<SecurityUserAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class Builder {
        private final SecurityUser securityUser;

        public Builder() {
            securityUser = new SecurityUser();
        }

        public Builder withNameAndPassword(final String username, final String password) {
            securityUser.setUsername(username);
            securityUser.setPassword(password);
            return this;
        }

        public Builder withUUID(final UUID uuid) {
            securityUser.setUuid(uuid);
            return this;
        }

        public Builder withEmail(final String email) {
            securityUser.setEmail(email);
            return this;
        }

        public SecurityUser build() {
            return securityUser;
        }
    }
}