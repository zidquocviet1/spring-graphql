package com.mqv.springgraphql.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees;

    public Department() {
    }

    public Department(Integer id, String name, Organization organization, Set<Employee> employees) {
        this.id = id;
        this.name = name;
        this.organization = organization;
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
