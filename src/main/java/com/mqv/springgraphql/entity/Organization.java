package com.mqv.springgraphql.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @OneToMany(mappedBy = "organization")
    private Set<Department> departments;
    @OneToMany(mappedBy = "organization")
    private Set<Employee> employees;

    public Organization() {
    }

    public Organization(Integer id, String name, Set<Department> departments, Set<Employee> employees) {
        this.id = id;
        this.name = name;
        this.departments = departments;
        this.employees = employees;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
