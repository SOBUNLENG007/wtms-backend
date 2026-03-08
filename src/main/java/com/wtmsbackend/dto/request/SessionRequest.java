package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SessionRequest {

    @NotBlank(message = "Session title is required")
    @Size(max = 100, message = "Session title cannot exceed 100 characters") // Matches your DB VARCHAR(100)
    private String title;

    @NotBlank(message = "Session description is required")
    @Size(max = 255, message = "Session description cannot exceed 255 characters") // Matches your DB VARCHAR(255)
    private String description;

    @NotNull(message = "Number of sessions is required")
    @Min(value = 1, message = "Number of sessions must be at least 1") // Prevents entering 0 or negative numbers
    private Integer numSession;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Department ID is required")
    @Min(value = 1, message = "Invalid Department ID")
    private Integer departmentId;

    @NotNull(message = "Instructor ID is required")
    @Min(value = 1, message = "Invalid Instructor ID")
    private Integer instructorId;
}