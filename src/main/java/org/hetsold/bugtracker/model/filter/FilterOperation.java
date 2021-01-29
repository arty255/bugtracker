package org.hetsold.bugtracker.model.filter;

public enum FilterOperation {
    EQUAL("equal"),
    LIKE("contains"),
    NOT_EQUAL("not equal"),
    MORE("more"),
    LESS("less"),
    BETWEEN("between");

    private String label;

    FilterOperation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}