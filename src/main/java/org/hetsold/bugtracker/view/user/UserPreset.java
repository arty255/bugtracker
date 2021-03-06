package org.hetsold.bugtracker.view.user;

import org.hetsold.bugtracker.model.SecurityUserAuthority;

import java.util.Set;

public enum UserPreset {
    ADMIN("admin", Set.of(SecurityUserAuthority.values())),
    SUPER_MODERATOR("super moderator", Set.of(
            SecurityUserAuthority.ROLE_LIST_ISSUES,
            SecurityUserAuthority.ROLE_EDIT_ISSUE,
            SecurityUserAuthority.ROLE_DELETE_ISSUE,
            SecurityUserAuthority.ROLE_ADD_ISSUE,
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_EDIT_TICKET_STATE,
            SecurityUserAuthority.ROLE_DELETE_TICKET,
            SecurityUserAuthority.ROLE_ADD_TICKET)),
    MODERATOR("moderator", Set.of(
            SecurityUserAuthority.ROLE_LIST_ISSUES,
            SecurityUserAuthority.ROLE_EDIT_ISSUE,
            SecurityUserAuthority.ROLE_ADD_ISSUE,
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_EDIT_TICKET_STATE,
            SecurityUserAuthority.ROLE_DELETE_TICKET,
            SecurityUserAuthority.ROLE_ADD_TICKET)),
    SUPER_USER("super user", Set.of(
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_EDIT_TICKET,
            SecurityUserAuthority.ROLE_DELETE_TICKET,
            SecurityUserAuthority.ROLE_ADD_TICKET)),
    USER("user", Set.of(
            SecurityUserAuthority.ROLE_LIST_TICKETS,
            SecurityUserAuthority.ROLE_ADD_TICKET));

    private final String label;
    private final Set<SecurityUserAuthority> securityUserAuthorities;

    UserPreset(String label, Set<SecurityUserAuthority> securityUserAuthorities) {
        this.label = label;
        this.securityUserAuthorities = securityUserAuthorities;
    }

    public String getLabel() {
        return label;
    }

    public Set<SecurityUserAuthority> getSecurityUserAuthorities() {
        return securityUserAuthorities;
    }
}