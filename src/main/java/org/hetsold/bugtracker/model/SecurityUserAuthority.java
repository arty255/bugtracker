package org.hetsold.bugtracker.model;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityUserAuthority implements GrantedAuthority {
    ROLE_LIST_USERS("ROLE_LIST_USERS", "list users"),
    ROLE_EDIT_USER("ROLE_EDIT_USER", "edit user"),
    ROLE_DELETE_USER("ROLE_DELETE_USER", "delete user"),
    ROLE_ADD_USER("ROLE_ADD_USER", "add user"),
    ROLE_LIST_ISSUES("ROLE_LIST_ISSUES", "list issues"),
    ROLE_EDIT_ISSUE("ROLE_EDIT_ISSUE", "edit issue"),
    ROLE_DELETE_ISSUE("ROLE_DELETE_ISSUE", "delete issue"),
    ROLE_ADD_ISSUE("ROLE_ADD_ISSUE", "ad issue"),
    ROLE_LIST_TICKETS("ROLE_LIST_TICKETS", "list tickets"),
    ROLE_EDIT_TICKET("ROLE_EDIT_TICKET", "edit ticket"),
    ROLE_EDIT_TICKET_STATE("ROLE_EDIT_TICKET_STATE", "edit ticket state"),
    ROLE_DELETE_TICKET("ROLE_DELETE_TICKET", "delete ticket"),
    ROLE_ADD_TICKET("ROLE_ADD_TICKET", "add ticket");

    SecurityUserAuthority(String authority, String label) {
        this.authority = authority;
        this.label = label;
    }

    private final String authority;
    private final String label;

    public String getLabel() {
        return label;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
