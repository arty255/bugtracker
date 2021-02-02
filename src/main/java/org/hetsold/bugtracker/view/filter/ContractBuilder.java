package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.model.IssueState;
import org.hetsold.bugtracker.model.Severity;
import org.hetsold.bugtracker.model.TicketResolveState;
import org.hetsold.bugtracker.model.TicketVerificationState;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ContractBuilder {
    private static final Map<String, Function<Object, ?>> mappingFunctionMap = Map.of(
            "String", Object::toString,
            "IssueState", i -> IssueState.valueOf(i.toString()),
            "Severity", i -> Severity.valueOf(i.toString()),
            "TicketResolveState", i -> TicketResolveState.valueOf(i.toString()),
            "TicketVerificationState", i -> TicketVerificationState.valueOf(i.toString())
    );

    public static Contract buildContact(List<DisplayableFieldFilter> list) {
        Contract contract = new Contract();
        Predicate<DisplayableFieldFilter> filterPredicate = item ->
                item.getFieldFilter().getValue() != null && item.getFieldFilter().getFilterOperation() != null;
        contract.getFilters()
                .addAll(list.stream()
                        .filter(filterPredicate)
                        .map(ContractBuilder::updateFieldFilter)
                        .collect(Collectors.toList()));
        return contract;
    }

    private static FieldFilter updateFieldFilter(DisplayableFieldFilter item) {
        Function<Object, ?> objectMappingFunction;
        FieldFilter filter = item.getFieldFilter();
        if (mappingFunctionMap.containsKey(item.getType())) {
            objectMappingFunction = mappingFunctionMap.get(item.getType());
            filter.setValue(objectMappingFunction.apply(filter.getValue()));
        } else {
            throw new IllegalArgumentException("unsupported argument type");
        }
        return item.getFieldFilter();
    }
}