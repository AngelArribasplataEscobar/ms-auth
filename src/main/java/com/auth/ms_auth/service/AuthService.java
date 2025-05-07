package com.auth.ms_auth.service;
import com.auth.ms_auth.dto.AuthRequest;
import com.auth.ms_auth.dto.AuthResponse;
import com.auth.ms_auth.dto.RegisterRequest;
import com.auth.ms_auth.entity.User;
import com.auth.ms_auth.repository.UserRepository;
import com.auth.ms_auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @file: AuthService
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User register(RegisterRequest req) {
        repo.findByUsername(req.getUsername())
                .ifPresent(u -> { throw new RuntimeException("El usuario ya existe"); });

        User u = User.builder()
                .username(req.getUsername())
                .password(encoder.encode(req.getPassword()))
                .role(req.getRole())
                .build();
        return repo.save(u);
    }
    public AuthResponse login(AuthRequest req) {
        // 1) Validar credenciales y obtener el Authentication
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        // 2) Sacar el UserDetails del principal
        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        // 3) Generar el token a partir de ese UserDetails
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
    public User validateToken(String token) {
        // 1) extraer username del token
        String username = jwtService.extractUsername(token);

        // 2) cargar usuario
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3) validar expiración y firma
        UserDetails ud = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        if (!jwtService.isTokenValid(token, ud)) {
            throw new RuntimeException("Token inválido o expirado");
        }

        return user;
    }
}
