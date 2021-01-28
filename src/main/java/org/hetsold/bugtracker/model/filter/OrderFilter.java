package org.hetsold.bugtracker.model.filter;

public class OrderFilter {
    private String fieldName;
    private boolean isAscending;

    public OrderFilter(String fieldName, boolean isAscending) {
        this.fieldName = fieldName;
        this.isAscending = isAscending;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isAscending() {
        return isAscending;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }
}