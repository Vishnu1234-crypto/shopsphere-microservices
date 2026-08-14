package com.shopsphere.api_gateway.config;

import com.shopsphere.api_gateway.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpStatus;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                unauthorizedEntryPoint()
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login"
                        ).permitAll()

                        // Everything else requires JWT
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {

        return (request, response, authException) -> {

            response.setStatus(
                    HttpStatus.UNAUTHORIZED.value()
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    """
                    {
                      "status": 401,
                      "error": "Unauthorized",
                      "message": "Authentication required"
                    }
                    """
            );
        };
    }
}