package com.company.employee_dashboard.controller;

import com.company.employee_dashboard.model.User;
import com.company.employee_dashboard.repository.UserRepository;
import com.company.employee_dashboard.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    // Login
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return ResponseEntity.status(401).body(error);
        }

        String token = jwtUtil.generateToken(username);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("role", userOpt.get().getRole());
        return ResponseEntity.ok(response);
    }

    // Register (for creating admin user)
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String role     = body.get("role") != null ? body.get("role") : "ADMIN";

        if (userRepository.findByUsername(username).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username already exists");
            return ResponseEntity.status(400).body(error);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        return ResponseEntity.ok(response);
    }
}