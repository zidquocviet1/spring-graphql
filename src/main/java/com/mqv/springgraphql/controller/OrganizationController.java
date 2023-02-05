package com.mqv.springgraphql.controller;

import com.mqv.springgraphql.entity.Organization;
import com.mqv.springgraphql.graphql.OrganizationInput;
import com.mqv.springgraphql.repository.OrganizationRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public record OrganizationController(OrganizationRepository organizationRepository) {
    @QueryMapping
    public List<Organization> organizations() {
        return organizationRepository.findAll();
    }

    @QueryMapping
    public Organization organization(@Argument Integer id) {
        return organizationRepository.findById(id)
                .orElseThrow();
    }

    @MutationMapping
    public Organization newOrganization(@Argument(name = "organizationInput") OrganizationInput organization) {
        return organizationRepository.save(new Organization(null, organization.name(),
                null, null));
    }
}
