package com.mqv.springgraphql.controller;

import com.daw.graphql.clientoperation.PartnerInventoryListQuery;
import com.mqv.springgraphql.client.DayAwayServiceClient;
import com.mqv.springgraphql.entity.Department;
import com.mqv.springgraphql.entity.Organization;
import com.mqv.springgraphql.graphql.DepartmentInput;
import com.mqv.springgraphql.repository.DepartmentRepository;
import com.mqv.springgraphql.repository.OrganizationRepository;
import com.mqv.springgraphql.repository.specification.DepartmentSpecification;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
//import org.springframework.security.access.prepost.PreAuthorize;
import com.ptw.graphql.clientoperation.IdCardTypesQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class DepartmentController {
    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final DayAwayServiceClient dayAwayServiceClient;

    public DepartmentController(DepartmentRepository departmentRepository, OrganizationRepository organizationRepository, DayAwayServiceClient dayAwayServiceClient) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
        this.dayAwayServiceClient = dayAwayServiceClient;
    }

    @QueryMapping
    public List<Department> departments(DataFetchingEnvironment environment) {
        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        if (selectionSet.contains("employees") && selectionSet.contains("organization")) {
            return departmentRepository.findAll(DepartmentSpecification.fetchEmployees()
                    .and(DepartmentSpecification.fetchOrganization()));
        }
        if (selectionSet.contains("employees")) {
            return departmentRepository.findAll(DepartmentSpecification.fetchEmployees());
        }
        if (selectionSet.contains("organization")) {
            return departmentRepository.findAll(DepartmentSpecification.fetchOrganization());
        }
        return departmentRepository.findAll();
    }

    @QueryMapping
    public Department department(@Argument Integer id) {
        return departmentRepository.findById(id).orElseThrow();
    }

    @MutationMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public Department newDepartment(@Argument(name = "departmentInput") DepartmentInput department) {
        Organization organization = organizationRepository.findById(department.organizationId())
                .orElseThrow();
        return departmentRepository.save(new Department(
                null, department.name(), organization, null
        ));
    }

    @GetMapping("/id-card-types")
    public CompletableFuture<List<IdCardTypesQuery.IdCardType>> get() {
        return dayAwayServiceClient.getIdCardTypes("SG");
    }

    @GetMapping("/partner-inventory")
    public CompletableFuture<List<PartnerInventoryListQuery.OnPartner>> getPartnerInventoryList() {
        return dayAwayServiceClient.getPartnerInventoryList();
    }
}
