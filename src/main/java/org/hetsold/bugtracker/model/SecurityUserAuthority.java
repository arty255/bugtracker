package org.hetsold.bugtracker.model;

import org.springframework.security.core.GrantedAuthority;

public enum SecurityUserAuthority implements GrantedAuthority {
    ROLE_LIST_USERS("ROLE_LIST_USERS", "list users"),
    ROLE_EDIT_USERS("ROLE_EDIT_USERS", "edit user"),
    ROLE_DELETE_USER("ROLE_DELETE_USER", "delete user"),
    ROLE_LIST_ISSUES("ROLE_LIST_ISSUES", "list issues"),
    ROLE_EDIT_ISSUE("ROLE_EDIT_ISSUE", "edit issue"),
    ROLE_DELETE_ISSUE("ROLE_DELETE_ISSUE", "delete issue"),
    ROLE_LIST_TICKETS("ROLE_LIST_TICKETS", "list tickets"),
    ROLE_EDIT_TICKET("ROLE_EDIT_TICKET", "edit ticket"),
    ROLE_DELETE_TICKET("ROLE_DELETE_TICKET", "delete ticket");

    SecurityUserAuthority(String authority, String label) {
        this.authority = authority;
        this.label = label;
    }

    private String authority;
    private String label;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
