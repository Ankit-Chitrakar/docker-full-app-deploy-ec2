package com.ankit.auth_service.service;

import com.ankit.auth_service.dto.request.LoginRequest;
import com.ankit.auth_service.dto.request.RefreshTokenRequest;
import com.ankit.auth_service.dto.request.RegisterRequest;
import com.ankit.auth_service.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
