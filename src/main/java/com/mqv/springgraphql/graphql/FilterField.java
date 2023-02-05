package com.mqv.springgraphql.graphql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public record FilterField(String operator, String value) {
}
