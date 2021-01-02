package org.hetsold.bugtracker.model;

public enum TicketResolveState {
    NotResolved("not resolved"),
    Resolving("resolving"),
    Resolved("resolved"),
    NotBeResolved("not be resolved");

    private String label;

    TicketResolveState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}