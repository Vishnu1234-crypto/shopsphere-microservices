package com.shopsphere.notification_service.service;

import com.shopsphere.notification_service.dto.NotificationRequest;
import com.shopsphere.notification_service.entity.Notification;
import com.shopsphere.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;

    public Notification sendNotification(NotificationRequest request) {

        Notification notification = Notification.builder()
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
        return repository.findById(id).orElseThrow();
    }

    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }
}