package org.hetsold.bugtracker.model;

public enum TicketVerificationState {
    VERIFIED("verified"),
    NOT_VERIFIED("not verified");

    private final String label;

    TicketVerificationState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
