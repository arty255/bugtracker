package org.hetsold.bugtracker.dao.util;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Contract {
    private Set<FieldFilter> fieldFilters;
    private List<OrderFilter> orderFilters;

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