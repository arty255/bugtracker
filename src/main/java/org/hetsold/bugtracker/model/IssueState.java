package org.hetsold.bugtracker.model;

/*
 * this enum describe Issue states
 */
public enum IssueState {
    OPEN("open"),
    REOPEN("reopen"),
    ASSIGNED("assigned"),
    FIXED("fixed");

    private String label;

    IssueState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}