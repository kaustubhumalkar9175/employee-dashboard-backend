package com.company.employee_dashboard.service;

import com.company.employee_dashboard.model.Employee;
import com.company.employee_dashboard.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repo;

    // Get all employees
    public List<Employee> getAllEmployees() {
        return repo.findAll();
    }

    // Get single employee by ID
    public Optional<Employee> getEmployeeById(Long id) {
        return repo.findById(id);
    }

    // Add new employee
    public Employee createEmployee(Employee employee) {
        return repo.save(employee);
    }

    // Update existing employee
    public Employee updateEmployee(Long id, Employee updated) {
        Employee existing = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setEmail(updated.getEmail());
        existing.setDepartment(updated.getDepartment());
        existing.setRole(updated.getRole());
        existing.setSalary(updated.getSalary());
        existing.setJoinDate(updated.getJoinDate());
        existing.setStatus(updated.getStatus());

        return repo.save(existing);
    }

    // Delete employee
    public void deleteEmployee(Long id) {
        repo.deleteById(id);
    }

    // Search employees
    public List<Employee> searchEmployees(String keyword) {
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            keyword, keyword
        );
    }

    // Filter by department
    public List<Employee> getByDepartment(String department) {
        return repo.findByDepartment(department);
    }

    // Dashboard stats
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long total = repo.count();
        long active = repo.findByStatus(Employee.Status.ACTIVE).size();

        // Department count for pie chart
        List<Object[]> deptCount = repo.countByDepartment();
        Map<String, Long> deptMap = new LinkedHashMap<>();
        for (Object[] row : deptCount) {
            deptMap.put((String) row[0], (Long) row[1]);
        }

        // Avg salary per department for bar chart
        List<Object[]> salaryData = repo.avgSalaryByDepartment();
        Map<String, Double> salaryMap = new LinkedHashMap<>();
        for (Object[] row : salaryData) {
            salaryMap.put((String) row[0], (Double) row[1]);
        }

        stats.put("totalEmployees", total);
        stats.put("activeEmployees", active);
        stats.put("inactiveEmployees", total - active);
        stats.put("departmentCount", deptMap);
        stats.put("avgSalaryByDepartment", salaryMap);

        return stats;
    }
}