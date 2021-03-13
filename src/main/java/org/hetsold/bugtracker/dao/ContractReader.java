package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.dao.util.FieldFilter;
import org.hetsold.bugtracker.dao.util.FilterOperation;
import org.hetsold.bugtracker.dao.util.OrderFilter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public final class ContractReader {
    private ContractReader() {
    }

    public static Predicate[] readContract(final Contract contract, final Root<?> root, final CriteriaBuilder criteriaBuilder) {
        if (contract == null) {
            return new Predicate[0];
        }
        final List<Predicate> predicateList = new ArrayList<>();
        contract.getFilters().forEach(
                item -> predicateList.add(createPredicate(item, root, criteriaBuilder))
        );
        return predicateList.toArray(new Predicate[0]);
    }

    private static Predicate createPredicate(final FieldFilter fieldFilter, final Root<?> root, final CriteriaBuilder criteriaBuilder) {
        if (fieldFilter.getFilterOperation() == FilterOperation.EQUAL) {
            return criteriaBuilder.equal(root.get(fieldFilter.getFieldName()), fieldFilter.getValue());
        } else if (fieldFilter.getFilterOperation() == FilterOperation.NOT_EQUAL) {
            return criteriaBuilder.notEqual(root.get(fieldFilter.getFieldName()), fieldFilter.getValue());
        } else if (fieldFilter.getFilterOperation() == FilterOperation.LIKE) {
            return criteriaBuilder.like(root.get(fieldFilter.getFieldName()), "%" + fieldFilter.getValue() + "%");
        }
        return null;
    }

    public static List<Order> getOrders(final Contract contract, final Root<?> root, final CriteriaBuilder criteriaBuilder) {
        final List<Order> orderList = new ArrayList<>();
        if (contract != null && contract.getOrderFilters() != null) {
            contract.getOrderFilters()
                    .forEach(item -> orderList.add(getOrderByFilterItem(item, root, criteriaBuilder)));
        }
        return orderList;
    }

    private static Order getOrderByFilterItem(final OrderFilter orderFilter, final Root<?> root, final CriteriaBuilder criteriaBuilder) {
        if (orderFilter.isAscending()) {
            return criteriaBuilder.asc(root.get(orderFilter.getFieldName()));
        } else {
            return criteriaBuilder.desc(root.get(orderFilter.getFieldName()));
        }
    }
}