package org.hetsold.bugtracker.view.filter;

import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.FilterOperation;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FilterComponentBuilder {
    private FilterComponentBuilder() {
    }

    private static final Map<String, String> labelMap = Map.of(
            "currentIssueState", "Issue state",
            "severity", "Severity",
            "verificationState", "Verification state",
            "resolveState", "Resolve state",
            "productVersion", "Product version"
    );

    public static Set<FieldMaskFilter> buildFieldMaskFilters(Class<?> aClass, String fieldNamesString) {
        String[] fieldNames = getNamesArray(fieldNamesString);
        return getFields(aClass).stream()
                .filter(item -> isSupportedField(item, fieldNames, FilterComponentBuilder::isFieldSupportedType))
                .map(FilterComponentBuilder::getDisplayableFieldFilter)
                .collect(Collectors.toSet());
    }

    public static List<FieldOrderFilter> buildFieldOrderFilters(Class<?> aClass, String orderNamesString) {
        String[] orderNames = getNamesArray(orderNamesString);
        Predicate<Field> datePredicate = item ->
                item.getType().equals(Date.class) || item.getType().equals(String.class) || item.getType().isEnum();
        return getFields(aClass).stream()
                .filter(item -> isSupportedField(item, orderNames, datePredicate))
                .map(FilterComponentBuilder::getOrderFieldFilter)
                .collect(Collectors.toList());
    }

    private static FieldOrderFilter getOrderFieldFilter(Field field) {
        return new FieldOrderFilter(field.getName(), null);
    }

    private static FieldMaskFilter getDisplayableFieldFilter(Field field) {
        return new FieldMaskFilter(
                new FieldFilter(field.getName(), null, null),
                field.getType().getSimpleName(),
                getFieldLabel(field.getName()),
                getAvailableOperations(field));
    }

    private static boolean isSupportedField(Field field, String[] fieldNames, Predicate<Field> predicate) {
        if (fieldNames.length > 0) {
            return predicate.test(field) &&
                    Arrays.stream(fieldNames).anyMatch(item -> item.equals(field.getName()));
        } else {
            return predicate.test(field);
        }
    }

    private static boolean isFieldSupportedType(Field field) {
        return field.getType().isEnum() || field.getType().equals(String.class)
                || field.getType().equals(Boolean.class) || field.getType().equals(Date.class);
    }

    private static String[] getNamesArray(String names) {
        Pattern pattern = Pattern.compile(" ");
        return pattern.splitAsStream(names).toArray(String[]::new);
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