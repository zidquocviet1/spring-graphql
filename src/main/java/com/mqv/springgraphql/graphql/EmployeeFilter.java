package com.mqv.springgraphql.graphql;

public record EmployeeFilter(FilterField salary, FilterField age, FilterField position) {
}
