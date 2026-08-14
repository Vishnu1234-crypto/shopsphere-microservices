package com.shopsphere.notification_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopsphere.notification_service.dto.NotificationRequest;
import com.shopsphere.notification_service.entity.Notification;
import com.shopsphere.notification_service.exception.ResourceNotFoundException;
import com.shopsphere.notification_service.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Notification sendNotification(
            NotificationRequest request) {

        Notification notification =
                Notification.builder()
                        .recipient(request.getRecipient())
                        .subject(request.getSubject())
                        .message(request.getMessage())
                        .status("SENT")
                        .build();

        return repository.save(notification);
    }

    public List<Notification> getAllNotifications() {

        return repository.findAll();
    }

    public Notification getNotification(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notification not found with id: "
                                        + id
                        )
                );
    }

    public void deleteNotification(Long id) {

        Notification notification =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: "
                                                + id
                                )
                        );

        repository.delete(notification);
    }
}