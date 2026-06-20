package com.shopsphere.auth_service.service;

import com.shopsphere.auth_service.dto.LoginRequest;
import com.shopsphere.auth_service.dto.LoginResponse;
import com.shopsphere.auth_service.dto.RegisterRequest;
import com.shopsphere.auth_service.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}