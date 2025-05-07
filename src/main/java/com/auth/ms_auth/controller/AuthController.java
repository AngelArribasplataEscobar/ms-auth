package com.auth.ms_auth.controller;
import com.auth.ms_auth.dto.AuthRequest;
import com.auth.ms_auth.dto.AuthResponse;
import com.auth.ms_auth.dto.RegisterRequest;
import com.auth.ms_auth.entity.User;
import com.auth.ms_auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * @file: AuthController
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest req) {
        User saved = authService.register(req);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        AuthResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }
    @GetMapping("/validate")
    public ResponseEntity<User> validateToken(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        User usuario = authService.validateToken(token);
        return ResponseEntity.ok(usuario);
    }

}
