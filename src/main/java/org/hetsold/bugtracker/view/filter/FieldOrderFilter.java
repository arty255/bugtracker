package org.hetsold.bugtracker.view.filter;

public class FieldOrderFilter {
    private String fieldName;
    private Boolean value;

    public FieldOrderFilter(String fieldName, Boolean value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }
}