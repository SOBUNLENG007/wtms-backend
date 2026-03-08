package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.NotificationRequest;
import com.wtmsbackend.dto.response.NotificationResponse;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get all notifications", description = "Retrieves a paginated list of all system notifications. Requires ADMIN role.")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getAllNotifications(page, size);
        return ResponseEntity.ok(ApiResponse.ok("Notifications fetched successfully", buildPagedResponse(notificationPage)));
    }

    @Operation(summary = "Get user notifications", description = "Retrieves all notifications for a specific user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserNotifications(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getUserNotifications(userId, page, size);
        return ResponseEntity.ok(ApiResponse.ok("User notifications fetched successfully", buildPagedResponse(notificationPage)));
    }

    @Operation(summary = "Get unread user notifications", description = "Retrieves only the UNREAD notifications for a specific user.")
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadUserNotifications(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationPage = notificationService.getUnreadUserNotifications(userId, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Unread notifications fetched successfully", buildPagedResponse(notificationPage)));
    }

    @Operation(summary = "Get notification by ID", description = "Retrieves specific notification details.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Integer id) {
        NotificationResponse notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification fetched successfully", notification));
    }

    @Operation(summary = "Create a notification", description = "Sends a new notification to a user. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> createNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationService.createNotification(request);
        return ResponseEntity.ok(ApiResponse.ok("Notification sent successfully", notification));
    }

    @Operation(summary = "Mark notification as read", description = "Marks a specific notification as read.")
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        NotificationResponse notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification marked as read", notification));
    }

    @Operation(summary = "Update a notification", description = "Updates a notification message. Requires ADMIN role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateNotification(
            @PathVariable Integer id,
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse notification = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Notification updated successfully", notification));
    }

    @Operation(summary = "Delete a notification", description = "Permanently deletes a notification.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted successfully", null));
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