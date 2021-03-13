package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.OrderFilter;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Severity;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ContractBuilder {
    private ContractBuilder() {
    }

    private static final Map<String, Function<Object, ?>> codedTypeToObjectMappingFunctionMap = Map.of(
            "String", Object::toString,
            "IssueState", i -> IssueState.valueOf(i.toString()),
            "Severity", i -> Severity.valueOf(i.toString()),
            "TicketResolveState", i -> TicketResolveState.valueOf(i.toString()),
            "TicketVerificationState", i -> TicketVerificationState.valueOf(i.toString())
    );

    public static Contract buildContact(Set<FieldMaskFilter> maskFilters, List<FieldOrderFilter> orderFilters) {
        return new Contract(getFiledFilters(maskFilters), getOrderFilters(orderFilters));
    }

    private static Set<FieldFilter> getFiledFilters(Set<FieldMaskFilter> list) {
        return list.stream()
                .filter(ContractBuilder::notEmptyMaskValueAndOperation)
                .map(ContractBuilder::mapFieldMaskFilterToFieldFilter)
                .collect(Collectors.toSet());
    }

    private static boolean notEmptyMaskValueAndOperation(FieldMaskFilter filter) {
        return filter.getFieldFilter().getValue() != null && filter.getFieldFilter().getFilterOperation() != null;
    }

    private static FieldFilter mapFieldMaskFilterToFieldFilter(FieldMaskFilter item) {
        Function<Object, ?> objectMappingFunction;
        FieldFilter filter = item.getFieldFilter();
        if (codedTypeToObjectMappingFunctionMap.containsKey(item.getType())) {
            objectMappingFunction = codedTypeToObjectMappingFunctionMap.get(item.getType());
            filter.setValue(objectMappingFunction.apply(filter.getValue()));
        } else {
            throw new IllegalArgumentException("unsupported argument type");
        }
        return item.getFieldFilter();
    }

    public static List<OrderFilter> getOrderFilters(List<FieldOrderFilter> list) {
        return list.stream()
                .filter(ContractBuilder::isNotEmptyOrder)
                .map(ContractBuilder::mapFieldOrderFilterToOrderFilter)
                .collect(Collectors.toList());
    }

    private static boolean isNotEmptyOrder(FieldOrderFilter orderFilter) {
        return !orderFilter.getFieldName().isEmpty() && orderFilter.getValue() != null;
    }

    private static OrderFilter mapFieldOrderFilterToOrderFilter(FieldOrderFilter fieldOrderFilter) {
        return new OrderFilter(fieldOrderFilter.getFieldName(), fieldOrderFilter.getValue());
    }
}