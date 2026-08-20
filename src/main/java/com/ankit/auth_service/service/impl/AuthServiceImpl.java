package com.ankit.auth_service.service.impl;

import com.ankit.auth_service.dto.request.LoginRequest;
import com.ankit.auth_service.dto.request.RefreshTokenRequest;
import com.ankit.auth_service.dto.request.RegisterRequest;
import com.ankit.auth_service.dto.response.AuthResponse;
import com.ankit.auth_service.entity.User;
import com.ankit.auth_service.repository.UserRepository;
import com.ankit.auth_service.service.AuthService;
import com.ankit.auth_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;

    private AuthResponse generateTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        // 1. Map DTO to Entity using ModelMapper
        User user = modelMapper.map(request, User.class);

        // 2. Overwrite the raw password with the encoded password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Set default role
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return generateTokens(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        return generateTokens(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String userEmail = jwtService.extractUsername(request.getRefreshToken());

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userEmail));

        String newAccessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}
