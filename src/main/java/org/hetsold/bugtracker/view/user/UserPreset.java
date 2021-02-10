package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.model.SecurityUserAuthority;

import java.util.Set;

public enum UserPreset {
    ADMIN("admin", Set.of(SecurityUserAuthority.values())),
    SUPER_MODERATOR("super moderator", Set.of(
            SecurityUserAuthority.ROLE_LIST_ISSUES,
            SecurityUserAuthority.ROLE_EDIT_ISSUE,
            SecurityUserAuthority.ROLE_DELETE_ISSUE,
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_EDIT_TICKET_STATE,
            SecurityUserAuthority.ROLE_DELETE_TICKET)
    ),
    MODERATOR("moderator", Set.of(
            SecurityUserAuthority.ROLE_LIST_ISSUES,
            SecurityUserAuthority.ROLE_EDIT_ISSUE,
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_EDIT_TICKET_STATE,
            SecurityUserAuthority.ROLE_DELETE_TICKET)
    ),
    SUPER_USER("super user", Set.of(
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_DELETE_TICKET
    )),
    USER("user", Set.of(
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET)
    );

    private String label;
    private Set<SecurityUserAuthority> securityUserAuthorities;

    UserPreset(String label, Set<SecurityUserAuthority> securityUserAuthorities) {
        this.label = label;
        this.securityUserAuthorities = securityUserAuthorities;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<SecurityUserAuthority> getSecurityUserAuthorities() {
        return securityUserAuthorities;
    }

    public void setSecurityUserAuthorities(Set<SecurityUserAuthority> securityUserAuthorities) {
        this.securityUserAuthorities = securityUserAuthorities;
    }
}