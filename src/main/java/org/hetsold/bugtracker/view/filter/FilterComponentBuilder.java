package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.model.filter.FieldFilter;
import org.hetsold.bugtracker.model.filter.FilterOperation;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterComponentBuilder {
    private static final Map<String, String> labelMap = Map.of(
            "currentIssueState", "Issue state",
            "severity", "Severity",
            "verificationState", "Verification state",
            "resolveState", "Resolve state",
            "productVersion", "Product version"
    );

    public static List<DisplayableFieldFilter> buildWrappers(Class<?> aClass) {
        return buildWrappers(aClass, "");
    }

    public static List<DisplayableFieldFilter> buildWrappers(Class<?> aClass, String fieldNames) {
        Predicate<Field> finalPredicate;
        Predicate<Field> fieldTypePredicate = field -> field.getType().isEnum()
                || field.getType().equals(String.class)
                || field.getType().equals(Boolean.class)
                || field.getType().equals(Date.class);
        finalPredicate = fieldTypePredicate;
        if (!fieldNames.isEmpty()) {
            finalPredicate = field -> getNamesArray(fieldNames).contains(field.getName()) && fieldTypePredicate.test(field);
        }
        List<Field> fields = getFields(aClass).stream()
                .filter(finalPredicate)
                .collect(Collectors.toList());
        Function<Field, DisplayableFieldFilter> mappingFunction = field -> new DisplayableFieldFilter(
                new FieldFilter(field.getName(), null, null),
                field.getType().getSimpleName(),
                getFieldLabel(field.getName()),
                getAvailableOperations(field));
        return fields.stream()
                .map(mappingFunction)
                .collect(Collectors.toList());
    }

    private static List<String> getNamesArray(String names) {
        Pattern pattern = Pattern.compile(" ");
        return pattern.splitAsStream(names).collect(Collectors.toList());
    }

    private static List<Field> getFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        do {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
        } while ((type = type.getSuperclass()) != null);
        return fields;
    }

    private static List<FilterOperation> getAvailableOperations(Field field) {
        Map<Class<?>, List<FilterOperation>> typeOperationMap =
                Map.of(Boolean.class, List.of(FilterOperation.EQUAL, FilterOperation.NOT_EQUAL),
                        String.class, List.of(FilterOperation.LIKE, FilterOperation.EQUAL),
                        Date.class, List.of(
                                FilterOperation.EQUAL,
                                FilterOperation.LESS,
                                FilterOperation.MORE,
                                FilterOperation.BETWEEN
                        )
                );
        if (field.getType().isEnum()) {
            return typeOperationMap.get(Boolean.class);
        } else if (field.getType().isPrimitive()) {
            return typeOperationMap.get(Date.class);
        } else {
            return typeOperationMap.get(field.getType());
        }
    }

    private static String getFieldLabel(String name) {
        String value;
        if ((value = labelMap.get(name)) != null) {
            return value;
        }
        return name;
    }
}