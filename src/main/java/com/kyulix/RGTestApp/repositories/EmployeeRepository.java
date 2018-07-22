package com.kyulix.RGTestApp.repositories;

import com.kyulix.RGTestApp.entities.Employee;
import com.kyulix.RGTestApp.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Iterable<Employee> getByWorkingOffice(Office office);
}
