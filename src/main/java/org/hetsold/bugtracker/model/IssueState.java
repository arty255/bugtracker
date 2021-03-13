package org.hetsold.bugtracker.model;

public enum IssueState {
    OPEN("open"),
    REOPEN("reopen"),
    ASSIGNED("assigned"),
    FIXED("fixed");

    private final String label;

    IssueState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}