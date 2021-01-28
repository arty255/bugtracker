package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.model.filter.Contract;
import org.hetsold.bugtracker.model.filter.FieldFilter;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ContractBuilder {
    public static Contract buildContact(List<DisplayableFieldFilter> list) {
        Contract contract = new Contract();
        Predicate<FieldFilter> filterPredicate = item ->
                item != null
                        && item.getValue() != null
                        && item.getFilterOperation() != null;

        contract.getFilters()
                .addAll(list.stream()
                        .map(ContractBuilder::getFieldFilter)
                        .filter(filterPredicate)
                        .collect(Collectors.toList()));
        return contract;
    }

    private static FieldFilter getFieldFilter(DisplayableFieldFilter item) {
        FieldFilter fieldFilter = item.getFieldFilter();
        fieldFilter.setValue(packEnumFromTypeAndValue(item.getType(),
                item.getFieldFilter().getValue())
        );
        return fieldFilter;
    }

    private static Object packEnumFromTypeAndValue(Class<?> typeClass, Object value) {
        if (typeClass.isEnum() && value != null) {
            Class<Enum<?>> enumClass = null;
            try {
                enumClass = (Class<Enum<?>>) Class.forName(typeClass.getName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < enumClass.getEnumConstants().length; i++) {
                if (enumClass.getEnumConstants()[i].name().equals(value.toString())) {
                    return enumClass.getEnumConstants()[i];
                }
            }
        }
        return null;
    }
}