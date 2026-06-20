package com.shopsphere.auth_service.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
public record UserResponse(
        String name,
        String email
) {}