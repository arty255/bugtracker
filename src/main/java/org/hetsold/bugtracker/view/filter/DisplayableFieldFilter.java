package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.model.filter.FieldFilter;
import org.hetsold.bugtracker.model.filter.FilterOperation;

import java.util.List;

public class DisplayableFieldFilter {
    private FieldFilter fieldFilter;
    private String type;
    private String label;
    private List<FilterOperation> availableOperations;

    public DisplayableFieldFilter(FieldFilter fieldFilter, String type, String label, List<FilterOperation> availableOperations) {
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
