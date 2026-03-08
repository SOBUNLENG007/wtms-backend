package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.NotificationRequest;
import com.wtmsbackend.dto.response.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<NotificationResponse> getAllNotifications(int page, int size);
    Page<NotificationResponse> getUserNotifications(Integer userId, int page, int size);
    Page<NotificationResponse> getUnreadUserNotifications(Integer userId, int page, int size);

    NotificationResponse getNotificationById(Integer id);
    NotificationResponse createNotification(NotificationRequest request);
    NotificationResponse updateNotification(Integer id, NotificationRequest request);

    // Special action
    NotificationResponse markAsRead(Integer id);

    void deleteNotification(Integer id);
}