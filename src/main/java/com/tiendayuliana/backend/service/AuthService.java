package com.tiendayuliana.backend.service;

import com.tiendayuliana.backend.dto.auth.LoginRequest;
import com.tiendayuliana.backend.dto.auth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
