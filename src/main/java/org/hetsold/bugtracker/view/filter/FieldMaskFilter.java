package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.FilterOperation;

import java.util.List;

public class FieldMaskFilter {
    private FieldFilter fieldFilter;
    private String type;
    private String label;
    private List<FilterOperation> availableOperations;

    public FieldMaskFilter(FieldFilter fieldFilter, String type, String label, List<FilterOperation> availableOperations) {
        this.fieldFilter = fieldFilter;
        this.type = type;
        this.label = label;
        this.availableOperations = availableOperations;
    }

    public FieldFilter getFieldFilter() {
        return fieldFilter;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public List<FilterOperation> getAvailableOperations() {
        return availableOperations;
    }
}
