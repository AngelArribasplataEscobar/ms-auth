package com.auth.ms_auth.controller;

import com.auth.ms_auth.entity.User;
import com.auth.ms_auth.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.auth.ms_auth.entity.Role;

import java.util.List;

/**
 * @file: TestController
 * @author: Angel Arribasplata
 * @created: 7/05/2025
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private final UserRepository repo;
    public TestController(UserRepository repo) { this.repo = repo; }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> todos = repo.findAllByRole(Role.USER);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAdmins() {
        List<User> todos = repo.findAllByRole(Role.ADMIN);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/superadmin")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<List<User>> getSuperadmins() {
        List<User> todos = repo.findAllByRole(Role.SUPERADMIN);
        return ResponseEntity.ok(todos);
    }
}
