package com.auth.ms_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @file: AuthResponse
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
