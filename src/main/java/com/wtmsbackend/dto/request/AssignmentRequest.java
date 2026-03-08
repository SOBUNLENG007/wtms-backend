package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentRequest {

    @NotBlank(message = "Assignment title is required")
    @Size(max = 255, message = "Assignment title cannot exceed 255 characters") // Matches DB VARCHAR(255)
    private String title;

    @NotBlank(message = "Assignment description is required")
    @Size(max = 3000, message = "Assignment description is too long") // Safety net for your TEXT column
    private String description;

    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be a future date and time") // Prevents setting a deadline in the past!
    private LocalDateTime deadline;

    @NotNull(message = "Total score is required")
    @Min(value = 1, message = "Total score must be at least 1") // Cannot have a 0-point assignment
    private Integer totalScore;

    @NotNull(message = "Session ID is required")
    @Min(value = 1, message = "Invalid Session ID") // Prevents 0 or negative foreign keys
    private Integer sessionId;
}