package com.kyulix.RGTestApp.resources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kyulix.RGTestApp.entities.Employee;
import org.springframework.hateoas.ResourceSupport;

import java.util.ArrayList;
import java.util.List;

public class EmployeeResource extends ResourceSupport {

    @JsonProperty("employees")
    private List<Employee> employees;

    @JsonCreator
    public EmployeeResource(List<Employee> employees) {
        this.employees = employees;
    }

    @JsonCreator
    public EmployeeResource(Employee employee) {
        this.employees = new ArrayList<>();
        this.employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return this.employees;
    }
}
