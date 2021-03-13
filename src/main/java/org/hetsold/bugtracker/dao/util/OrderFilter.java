package org.hetsold.bugtracker.dao.util;

public class OrderFilter {
    private final String fieldName;
    private final boolean ascending;

    public OrderFilter(final String fieldName, final boolean ascending) {
        this.fieldName = fieldName;
        this.ascending = ascending;
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isAscending() {
        return ascending;
    }
}