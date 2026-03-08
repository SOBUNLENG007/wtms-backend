package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NotificationRequest {

    @NotNull(message = "User ID (Recipient) is required")
    @Min(value = 1, message = "Invalid User ID") // Prevents sending 0 or negative IDs
    private Integer userId;

    @NotBlank(message = "Notification message is required")
    @Size(max = 255, message = "Notification message cannot exceed 255 characters") // Matches DB length
    private String message;
}