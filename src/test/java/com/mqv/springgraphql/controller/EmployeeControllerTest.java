package com.mqv.springgraphql.controller;

import com.mqv.springgraphql.entity.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureGraphQlTester
class EmployeeControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void addEmployee() {
        String mutation = """
                                mutation {
                    newEmployee(employeeInput: {
                        firstName: "Viet",
                        middleName: "Quoc",
                        lastName: "Mai",
                        position: "Game Developer",
                        salary: 50000000,
                        age: 28,
                        organizationId: 1,
                        departmentId: 1
                    }) {
                        id,
                        firstName,
                        lastName
                    }
                    }
                """.trim();

        Employee employee = graphQlTester.document(mutation)
                .execute()
                .path("data.newEmployee")
                .entity(Employee.class)
                .get();

        assertNotNull(employee);
        assertNotNull(employee.getId());
    }

    @Test
    void findAllEmployee() {
        String query = """
                query {
                    employees {
                        id,
                        firstName,
                        middleName,
                        salary,
                        age,
                        position
                    }
                }
                """.trim();

        List<Employee> employeeList = graphQlTester.document(query)
                .execute()
                .path("data.employees[*]")
                .entityList(Employee.class)
                .get();

        assertFalse(employeeList.isEmpty());
        assertNotNull(employeeList.get(0));
        assertNotNull(employeeList.get(0).getId());
        assertNotNull(employeeList.get(0).getFirstName());
    }

    @Test
    void findById() {
        String query = """
                query($employeeId: ID!) {
                    employee(id: $employeeId) {
                        id,
                        firstName,
                        middleName,
                        lastName
                    }
                }
                """.trim();

        Employee employee = graphQlTester.document(query)
                .variable("employeeId", 1111)
                .execute()
                .path("data.employee")
                .entity(Employee.class)
                .get();

        assertNotNull(employee);
        assertNotNull(employee.getId());
    }

    @Test
    void findWithFilter() {
        int filteredSalary = 25_000_000;
        String filteredPosition = "Developer";

        String query = """
                query($filteredSalary: String!, $filteredPosition: String!) {
                    employeesWithFilter(filter: {
                        salary: {
                            operator: "gt",
                            value: $filteredSalary
                        },
                        position: {
                            operator: "like",
                            value: $filteredPosition
                        }
                    }) {
                        id,
                        firstName,
                        lastName,
                        salary,
                        position
                    }
                }
                """.trim();

        List<Employee> employees = graphQlTester.document(query)
                .variable("filteredSalary", filteredSalary)
                .variable("filteredPosition", filteredPosition)
                .execute()
                .path("data.employeesWithFilter[*]")
                .entityList(Employee.class)
                .get();

        assertFalse(employees.isEmpty());
        assertNotNull(employees.get(0));
        assertNotNull(employees.get(0).getId());

        assertTrue(employees.get(0).getSalary() >= filteredSalary);
    }
}