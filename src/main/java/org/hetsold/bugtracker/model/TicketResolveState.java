package org.hetsold.bugtracker.model;

public enum TicketResolveState {
    NOT_RESOLVED("not resolved"),
    RESOLVING("resolving"),
    RESOLVED("resolved"),
    NOT_BE_RESOLVED("not be resolved");

    private final String label;

    TicketResolveState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}