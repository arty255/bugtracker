package org.hetsold.bugtracker.view.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SortedColumnsContainer {
    private List<FieldOrderFilter> acceptableOrderFilters;
    private List<FieldOrderFilter> orderFilters;

    public SortedColumnsContainer(List<FieldOrderFilter> acceptableOrderFilters) {
        this.acceptableOrderFilters = acceptableOrderFilters;
        orderFilters = new ArrayList<>();
    }

    private FieldOrderFilter getOrderFilter(String name) {
        return acceptableOrderFilters.stream()
                .filter(item -> item.getFieldName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private FieldOrderFilter getSelectedOrderFilter(String name) {
        return orderFilters.stream()
                .filter(item -> item.getFieldName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean isAscending(String columnKey) {
        return getSelectedOrderFilter(columnKey).getValue();
    }

    public void addColumnNameToOrder(String columnKey) {
        FieldOrderFilter fieldOrderFilter = getOrderFilter(columnKey);
        fieldOrderFilter.setValue(true);
        orderFilters.add(fieldOrderFilter);
    }

    public boolean containsColumnKeyInFilters(String columnKey) {
        return orderFilters.stream().anyMatch(item -> item.getFieldName().equals(columnKey));
    }

    public int getColumnKeyIndex(String columnKey) {
        FieldOrderFilter fieldOrderFilter = getSelectedOrderFilter(columnKey);
        if (fieldOrderFilter != null) {
            return orderFilters.indexOf(fieldOrderFilter);
        }
        return 0;
    }

    public void twoStateToggle(String columnKey) {
        if (!containsColumnKeyInFilters(columnKey)) {
            orderFilters.add(getOrderFilter(columnKey));
        }
        FieldOrderFilter fieldOrderFilter = getSelectedOrderFilter(columnKey);
        if (fieldOrderFilter.getValue() == null) {
            fieldOrderFilter.setValue(true);
        } else {
            fieldOrderFilter.setValue(!fieldOrderFilter.getValue());
        }
    }

    public void threeStateToggle(String columnKey) {
        if (!containsColumnKeyInFilters(columnKey)) {
            orderFilters.add(getOrderFilter(columnKey));
        }
        FieldOrderFilter fieldOrderFilter = getSelectedOrderFilter(columnKey);
        if (fieldOrderFilter.getValue() == null) {
            fieldOrderFilter.setValue(true);
        } else if (fieldOrderFilter.getValue()) {
            fieldOrderFilter.setValue(false);
        } else {
            fieldOrderFilter.setValue(null);
        }
    }

    public void removeColumnNameFromOrder(String columnKey) {
        orderFilters.removeIf(item -> item.getFieldName().equals(columnKey));
    }

    public List<FieldOrderFilter> getFinalOrderFilters() {
        return orderFilters.stream()
                .filter(item -> item.getValue() != null)
                .collect(Collectors.toList());
    }
}