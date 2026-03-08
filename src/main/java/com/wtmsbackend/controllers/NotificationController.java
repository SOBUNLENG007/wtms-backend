package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.NotificationRequest;
import com.wtmsbackend.dto.response.NotificationResponse;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Notifications", description = "Endpoints for managing system and user notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get all notifications", description = "Retrieves a paginated list of all system notifications. Requires ADMIN role.")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getAllNotifications(page, size);

        ApiResponse<PagedResponse<NotificationResponse>> response = ApiResponse.<PagedResponse<NotificationResponse>>builder()
                .message("Notifications fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(notificationPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get user notifications", description = "Retrieves all notifications for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getUserNotifications(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getUserNotifications(userId, page, size);

        ApiResponse<PagedResponse<NotificationResponse>> response = ApiResponse.<PagedResponse<NotificationResponse>>builder()
                .message("User notifications fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(notificationPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get unread user notifications", description = "Retrieves only the UNREAD notifications for a specific user.")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<PagedResponse<NotificationResponse>>> getUnreadUserNotifications(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getUnreadUserNotifications(userId, page, size);

        ApiResponse<PagedResponse<NotificationResponse>> response = ApiResponse.<PagedResponse<NotificationResponse>>builder()
                .message("Unread notifications fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(notificationPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get notification by ID", description = "Retrieves specific notification details.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(@PathVariable Integer id) {
        NotificationResponse notification = notificationService.getNotificationById(id);

        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .message("Notification fetched successfully!")
                .success(true)
                .payload(notification)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a notification", description = "Sends a new notification to a user. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationService.createNotification(request);

        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .message("Notification sent successfully!")
                .success(true)
                .payload(notification)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read.")
    @PutMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Integer id) {
        NotificationResponse notification = notificationService.markAsRead(id);

        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .message("Notification marked as read successfully!")
                .success(true)
                .payload(notification)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a notification", description = "Updates a notification message. Requires ADMIN role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
            @PathVariable Integer id,
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationService.updateNotification(id, request);

        ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                .message("Notification updated successfully!")
                .success(true)
                .payload(notification)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a notification", description = "Permanently deletes a notification.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Notification deleted successfully!")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    private PagedResponse<NotificationResponse> buildPagedResponse(Page<NotificationResponse> page) {
        return PagedResponse.<NotificationResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}