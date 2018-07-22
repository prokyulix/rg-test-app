package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kyulix.RGTestApp.entities.Employee;
import org.springframework.hateoas.ResourceSupport;

public class EmployeeResource extends ResourceSupport {

    @JsonProperty("employees")
    private Iterable<Employee> employees;

    @JsonCreator
    public EmployeeResource(Iterable<Employee> employees) {
        this.employees = employees;
    }

    public Iterable<Employee> getEmployees() {
        return this.employees;
    }
}
