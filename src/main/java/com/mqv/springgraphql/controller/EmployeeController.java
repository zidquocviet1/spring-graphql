package com.mqv.springgraphql.controller;

import com.mqv.springgraphql.entity.Department;
import com.mqv.springgraphql.entity.Employee;
import com.mqv.springgraphql.entity.Organization;
import com.mqv.springgraphql.graphql.EmployeeFilter;
import com.mqv.springgraphql.graphql.EmployeeInput;
import com.mqv.springgraphql.repository.DepartmentRepository;
import com.mqv.springgraphql.repository.EmployeeRepository;
import com.mqv.springgraphql.repository.OrganizationRepository;
import com.mqv.springgraphql.repository.specification.EmployeeSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public record EmployeeController(EmployeeRepository employeeRepository,
                                 DepartmentRepository departmentRepository,
                                 OrganizationRepository organizationRepository) {
    @QueryMapping
    public List<Employee> employees() {
        return employeeRepository.findAll();
    }

    @QueryMapping
    public Employee employee(@Argument Integer id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    @MutationMapping
    public Employee newEmployee(@Argument(name = "employeeInput") EmployeeInput employee) {
        Department department = departmentRepository.findById(employee.departmentId())
                .orElseThrow();
        Organization organization = organizationRepository.findById(employee.organizationId())
                .orElseThrow();
        return employeeRepository.save(new Employee(
                null,
                employee.firstName(),
                employee.middleName(),
                employee.lastName(),
                employee.position(),
                employee.salary(),
                employee.age(),
                department,
                organization
        ));
    }

    @QueryMapping("employeesWithFilter")
    public List<Employee> getMultipleEmployeeByFilter(@Argument(name = "filter") EmployeeFilter filter) {
        Specification<Employee> specification = EmployeeSpecification.withFilter(filter);
        if (specification != null) {
            return employeeRepository.findAll(specification);
        } else {
            return employeeRepository.findAll();
        }
    }
}
