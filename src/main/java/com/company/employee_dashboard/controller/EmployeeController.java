package com.company.employee_dashboard.controller;

import com.company.employee_dashboard.model.Employee;
import com.company.employee_dashboard.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.company.employee_dashboard.service.FileStorageService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:3000")   // Allow React to call this API
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    // GET all employees
    @GetMapping
    public List<Employee> getAll() {
        return service.getAllEmployees();
    }

    // GET single employee
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        return service.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create employee
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.createEmployee(employee);
    }

    // PUT update employee
    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(
            @PathVariable Long id,
            @RequestBody Employee employee) {
        try {
            return ResponseEntity.ok(service.updateEmployee(id, employee));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // GET search employees
    @GetMapping("/search")
    public List<Employee> search(@RequestParam String keyword) {
        return service.searchEmployees(keyword);
    }

    // GET filter by department
    @GetMapping("/department/{dept}")
    public List<Employee> byDepartment(@PathVariable String dept) {
        return service.getByDepartment(dept);
    }

    // GET dashboard stats (for charts)
    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return service.getDashboardStats();
    }

    @Autowired
    private FileStorageService fileStorageService;

    @Value("${file.upload-dir}")
    private String uploadDir;

// POST upload photo for an employee
    @PostMapping("/{id}/photo")
    public ResponseEntity<Employee> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            Employee emp = service.getEmployeeById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // Delete old photo if exists
            if (emp.getPhoto() != null) {
                fileStorageService.deleteFile(emp.getPhoto());
            }

            String filename = fileStorageService.saveFile(file);
            emp.setPhoto(filename);
            Employee saved = service.createEmployee(emp);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

// GET serve the photo file
    @GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
