package com.mqv.springgraphql.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String position;
    private Integer salary;
    private Integer age;
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    public Employee() {
    }

    public Employee(Integer id, String firstName, String middleName, String lastName, String position, Integer salary, Integer age, Department department, Organization organization) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.position = position;
        this.salary = salary;
        this.age = age;
        this.department = department;
        this.organization = organization;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
