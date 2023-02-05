package com.mqv.springgraphql.graphql;

public record EmployeeInput(
        String firstName,
        String middleName,
        String lastName,
        String position,
        Integer salary,
        Integer age,
        Integer organizationId,
        Integer departmentId
) {
}
