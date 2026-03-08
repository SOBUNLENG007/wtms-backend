package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.Data;

@Data
public class SubmissionRequest {

    @NotNull(message = "Assignment ID is required")
    private Integer assignmentId;

    @NotNull(message = "Employee (User ID) is required")
    private Integer employeeId;

    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "File URL cannot exceed 500 characters") // Matches DB length
    @URL(message = "Must be a valid URL (e.g., https://drive.google.com/...)") // Ensures it's an actual web link
    private String fileUrl;
}