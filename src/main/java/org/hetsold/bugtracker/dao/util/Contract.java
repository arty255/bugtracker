package org.hetsold.bugtracker.dao.util;

import java.util.List;
import java.util.Set;

public class Contract {
    private final Set<FieldFilter> fieldFilters;
    private final List<OrderFilter> orderFilters;

    public Contract(Set<FieldFilter> fieldFilters, List<OrderFilter> orderFilters) {
        this.fieldFilters = fieldFilters;
        this.orderFilters = orderFilters;
    }

    public Set<FieldFilter> getFilters() {
        return fieldFilters;
    }

    public List<OrderFilter> getOrderFilters() {
        return orderFilters;
    }
}