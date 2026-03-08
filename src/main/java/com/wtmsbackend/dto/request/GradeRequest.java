package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GradeRequest {

    @NotNull(message = "Score is required")
    @Min(value = 0, message = "Score cannot be negative") // Prevents accidental negative grades
    private Integer score;

    // It's optional, so no @NotBlank, but we still protect the database size!
    @Size(max = 1000, message = "Feedback cannot exceed 1000 characters")
    private String feedback;
}