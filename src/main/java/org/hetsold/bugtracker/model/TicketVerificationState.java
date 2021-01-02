package org.hetsold.bugtracker.model;

public enum TicketVerificationState {
    Verified("verified"),
    NotVerified("not verified");

    private String label;

    TicketVerificationState(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
