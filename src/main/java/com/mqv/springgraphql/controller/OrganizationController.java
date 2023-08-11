package com.mqv.springgraphql.controller;

import com.mqv.springgraphql.entity.Organization;
import com.mqv.springgraphql.graphql.OrganizationInput;
import com.mqv.springgraphql.repository.OrganizationRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrganizationController {
    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

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
//    @PreAuthorize("hasRole('ADMIN')")
    public Organization newOrganization(@Argument(name = "organizationInput") OrganizationInput organization) {
        return organizationRepository.save(new Organization(null, organization.name(),
                null, null));
    }
}
