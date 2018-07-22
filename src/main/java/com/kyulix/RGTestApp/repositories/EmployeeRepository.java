package com.kyulix.RGTestApp.repositories;

import com.kyulix.RGTestApp.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
