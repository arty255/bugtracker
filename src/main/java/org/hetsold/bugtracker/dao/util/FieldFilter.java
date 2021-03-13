package org.hetsold.bugtracker.dao.util;

public class FieldFilter {
    private final String fieldName;
    private FilterOperation filterOperation;
    private Object value;

    public FieldFilter(final String fieldName, final FilterOperation filterOperation, final Object value) {
        this.fieldName = fieldName;
        this.filterOperation = filterOperation;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFilterOperation(final FilterOperation filterOperation) {
        this.filterOperation = filterOperation;
    }

    public FilterOperation getFilterOperation() {
        return filterOperation;
    }

    public void setValue(final Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}