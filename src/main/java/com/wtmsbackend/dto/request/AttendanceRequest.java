package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AttendanceRequest {

    @NotNull(message = "User ID (Trainee) is required")
    @Min(value = 1, message = "Invalid User ID") // Prevents 0 or negative IDs
    private Integer userId;

    @NotNull(message = "Session ID is required")
    @Min(value = 1, message = "Invalid Session ID") // Prevents 0 or negative IDs
    private Integer sessionId;

    @NotNull(message = "Attendance date is required")
    // Optional: You could add @PastOrPresent(message = "Cannot mark attendance for the future") here!
    private LocalDate attendanceDate;

    @NotBlank(message = "Status is required")
    @Size(max = 50, message = "Status cannot exceed 50 characters")
    @Pattern(
            regexp = "^(PRESENT|ABSENT|LATE|EXCUSED)$",
            message = "Status must be exactly PRESENT, ABSENT, LATE, or EXCUSED"
    ) // Forces clean data for your charts/reports!
    private String status;

    @NotNull(message = "Marked By (Trainer User ID) is required")
    @Min(value = 1, message = "Invalid Trainer ID") // Prevents 0 or negative IDs
    private Integer markedById;
}