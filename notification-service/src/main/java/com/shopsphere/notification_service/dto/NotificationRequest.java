package com.shopsphere.notification_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    private String recipient;

    private String subject;

    @NotBlank(message = "Message cannot be empty")
    private String message;
}