package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    Page<Notification> findAll(Pageable pageable);

    // Fetch all notifications for a specific user
    Page<Notification> findByUserId(Integer userId, Pageable pageable);

    // Fetch only UNREAD notifications for a specific user
    Page<Notification> findByUserIdAndIsReadFalse(Integer userId, Pageable pageable);
}