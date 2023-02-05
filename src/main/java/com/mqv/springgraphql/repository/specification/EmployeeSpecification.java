package com.mqv.springgraphql.repository.specification;

import com.mqv.springgraphql.entity.Employee;
import com.mqv.springgraphql.graphql.EmployeeFilter;
import com.mqv.springgraphql.graphql.FilterField;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;

public record EmployeeSpecification() {
    private static Specification<Employee> bySalary(FilterField filterField) {
        return apply(filterField.operator(), filterField.value(), "salary");
    }

    private static Specification<Employee> byAge(FilterField filterField) {
        return apply(filterField.operator(), filterField.value(), "age");
    }

    private static Specification<Employee> byPosition(FilterField filterField) {
        return apply(filterField.operator(), filterField.value(), "position");
    }

    private static Specification<Employee> apply(String operator, String value, String key) {
        return (root, query, builder) -> switch (operator) {
            case "gt" -> builder.greaterThanOrEqualTo(root.get(key), value);
            case "lt" -> builder.lessThan(root.get(key), value);
            case "like" -> builder.like(root.get(key), "%" + value + "%");
            default -> null;
        };
    }

    @Nullable
    public static Specification<Employee> withFilter(EmployeeFilter filter) {
        Specification<Employee> spec = null;
        if (filter.age() != null) {
            spec = byAge(filter.age());
        }
        if (filter.position() != null) {
            spec = (spec == null ? byPosition(filter.position()) : spec.and(byPosition(filter.position())));
        }
        if (filter.salary() != null) {
            spec = (spec == null ? bySalary(filter.salary()) : spec.and(bySalary(filter.salary())));
        }
        return spec;
    }
}
