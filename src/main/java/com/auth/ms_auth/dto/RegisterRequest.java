package com.auth.ms_auth.dto;

import com.auth.ms_auth.entity.Role;
import lombok.Data;

/**
 * @file: RegisterRequest
 * @author: Angel Arribasplata
 * @created: 3/05/2025
 */
@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
