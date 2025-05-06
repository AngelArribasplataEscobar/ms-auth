package com.auth.ms_auth.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
/**
 * @file: JwtService
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;              // definido en application.properties o en Vault

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationInMs;        // p. ej. 3600000 = 1h

    // Extrae el username (subject) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae cualquier claim usando una función
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // Genera un token sin claims adicionales
    public String generateToken(UserDetails userDetails) {
        return buildToken(claims -> {}, userDetails.getUsername());
    }

    // Genera un token con claims extra si lo necesitas
    public String generateToken(
            java.util.function.Consumer<Claims> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails.getUsername());
    }

    // Valida que el token coincida con el userDetails y no esté expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Crea el objeto Authentication para inyectar en el contexto
    public UsernamePasswordAuthenticationToken getAuthenticationToken(
            String token,
            UserDetails userDetails
    ) {
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    // ——— Métodos privados ———

    private String buildToken(
            java.util.function.Consumer<Claims> extraClaims,
            String subject
    ) {
        Claims claims = Jwts.claims();
        extraClaims.accept(claims);

        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
