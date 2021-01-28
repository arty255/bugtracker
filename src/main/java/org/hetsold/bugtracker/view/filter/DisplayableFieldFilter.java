package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.model.filter.FieldFilter;
import org.hetsold.bugtracker.model.filter.FilterOperation;

import java.util.List;

public class DisplayableFieldFilter {
    private FieldFilter fieldFilter;
    private Class<?> type;
    private List<FilterOperation> availableOperations;

    public DisplayableFieldFilter(FieldFilter fieldFilter, Class<?> type, List<FilterOperation> availableOperations) {
        this.fieldFilter = fieldFilter;
        this.type = type;
        this.availableOperations = availableOperations;
    }

    public FieldFilter getFieldFilter() {
        return fieldFilter;
    }

    public Class<?> getType() {
        return type;
    }

    public List<FilterOperation> getAvailableOperations() {
        return availableOperations;
    }
}
