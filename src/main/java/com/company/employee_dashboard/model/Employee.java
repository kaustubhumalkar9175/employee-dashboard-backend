package com.company.employee_dashboard.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String department;
    private String role;
    private BigDecimal salary;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "photo")
private String photo;

    public enum Status {
        ACTIVE, INACTIVE
    }

    // ✅ Getters
    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getDepartment() { return department; }
    public String getRole() { return role; }
    public BigDecimal getSalary() { return salary; }
    public LocalDate getJoinDate() { return joinDate; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPhoto() { return photo; }

    // ✅ Setters
    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setDepartment(String department) { this.department = department; }
    public void setRole(String role) { this.role = role; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }
    public void setStatus(Status status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setPhoto(String photo) { this.photo = photo; }
}