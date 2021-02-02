package org.hetsold.bugtracker.dao.util;

import java.util.ArrayList;
import java.util.List;

public class Contract {
    private List<FieldFilter> fieldFilters;
    private OrderFilter orderFilter;

    public Contract() {
        fieldFilters = new ArrayList<>();
    }

    public List<FieldFilter> getFilters() {
        return fieldFilters;
    }

    public OrderFilter getOrderFilter() {
        return orderFilter;
    }
}