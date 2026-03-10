package com.company.employee_dashboard.repository;

import com.company.employee_dashboard.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Search by name or email
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
        String firstName, String lastName
    );

    // Filter by department
    List<Employee> findByDepartment(String department);

    // Filter by status
    List<Employee> findByStatus(Employee.Status status);

    // Count employees per department (for charts)
    @Query("SELECT e.department, COUNT(e) FROM Employee e GROUP BY e.department")
    List<Object[]> countByDepartment();

    // Average salary per department (for charts)
    @Query("SELECT e.department, AVG(e.salary) FROM Employee e GROUP BY e.department")
    List<Object[]> avgSalaryByDepartment();
}