package com.mqv.springgraphql.repository.specification;

import com.mqv.springgraphql.entity.Department;
import com.mqv.springgraphql.entity.Employee;
import com.mqv.springgraphql.entity.Organization;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public record DepartmentSpecification() {
    public static Specification<Department> fetchEmployees() {
        return (root, query, criteriaBuilder) -> {
            Fetch<Department, Employee> fetch = root.fetch("employees", JoinType.LEFT);
            Join<Department, Employee> join = (Join<Department, Employee>) fetch;
            return join.getOn();
        };
    }

    public static Specification<Department> fetchOrganization() {
        return (root, query, criteriaBuilder) -> {
            Fetch<Department, Organization> fetch = root.fetch("organization", JoinType.LEFT);
            Join<Department, Organization> join = (Join<Department, Organization>) fetch;
            return join.getOn();
        };
    }
}
