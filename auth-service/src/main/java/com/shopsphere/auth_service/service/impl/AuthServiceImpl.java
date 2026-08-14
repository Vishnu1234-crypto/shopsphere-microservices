package com.shopsphere.auth_service.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopsphere.auth_service.dto.LoginRequest;
import com.shopsphere.auth_service.dto.LoginResponse;
import com.shopsphere.auth_service.dto.RegisterRequest;
import com.shopsphere.auth_service.dto.RegisterResponse;
import com.shopsphere.auth_service.entity.User;
import com.shopsphere.auth_service.exception.DuplicateResourceException;
import com.shopsphere.auth_service.exception.InvalidCredentialsException;
import com.shopsphere.auth_service.exception.ResourceNotFoundException;
import com.shopsphere.auth_service.repository.UserRepository;
import com.shopsphere.auth_service.security.JwtService;
import com.shopsphere.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    @Override
    public RegisterResponse register(
            RegisterRequest request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists: "
                            + request.getEmail()
            );
        }

        User user = User.builder()
                .name(request.getUsername())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                )
                .role("USER")
                .build();

        User savedUser =
                userRepository.save(user);

        return new RegisterResponse(
                "User registered successfully",
                savedUser.getName(),
                savedUser.getEmail()
        );
    }


    @Override
    public LoginResponse login(
            LoginRequest request) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found with email: "
                                                + request.getEmail()
                                )
                        );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return new LoginResponse(
                "Login successful",
                token
        );
    }
}