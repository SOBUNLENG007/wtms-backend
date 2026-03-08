package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import lombok.Data;

@Data
public class MaterialRequest {

    @NotBlank(message = "Material title is required")
    @Size(max = 255, message = "Material title cannot exceed 255 characters") // Protects your DB
    private String title;

    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "File URL cannot exceed 500 characters") // Matches DB length
    @URL(message = "Must be a valid URL (e.g., https://drive.google.com/...)") // Ensures it's an actual clickable link
    private String fileUrl;

    @NotNull(message = "Session ID is required")
    @Min(value = 1, message = "Invalid Session ID") // Prevents 0 or negative IDs
    private Integer sessionId;

    @NotNull(message = "Trainer ID is required")
    @Min(value = 1, message = "Invalid Trainer ID") // Prevents 0 or negative IDs
    private Integer trainerId;
}