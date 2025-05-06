package com.auth.ms_auth.service;
import com.auth.ms_auth.dto.RegisterRequest;
import com.auth.ms_auth.entity.User;
import com.auth.ms_auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @file: AuthService
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(RegisterRequest req) {
        if (repo.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        return repo.save(u);
    }
}
