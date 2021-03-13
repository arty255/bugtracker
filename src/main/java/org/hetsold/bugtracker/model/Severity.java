package org.hetsold.bugtracker.model;

public enum Severity {
    UNRATED("unrated"),
    TRIVIAL("trivial"),
    MINOR("minor"),
    MEDIUM("medium"),
    CRITICAL("critical"),
    BLOCKER("blocker");

    private final String label;

    Severity(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}