package org.hetsold.bugtracker.dao.util;

public class FieldFilter {
    private String fieldName;
    private FilterOperation filterOperation;
    private Object value;

    public FieldFilter(String fieldName, FilterOperation filterOperation, Object value) {
        this.fieldName = fieldName;
        this.filterOperation = filterOperation;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFilterOperation(FilterOperation filterOperation) {
        this.filterOperation = filterOperation;
    }

    public FilterOperation getFilterOperation() {
        return filterOperation;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}