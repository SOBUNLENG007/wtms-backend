package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    // Removed @NotNull because @NotBlank automatically checks for null AND empty strings
    @NotBlank(message = "First name is required")
    @Size(max = 120, message = "First name cannot exceed 120 characters") // Matches your DB VARCHAR(120)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "First name must contain only letters and spaces")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 120, message = "Last name cannot exceed 120 characters") // Matches your DB VARCHAR(120)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Last name must contain only letters and spaces")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email cannot exceed 150 characters") // Matches your DB VARCHAR(150)
    @Email(regexp = "^[A-Za-z0-9._%+-]+@gmail\\.com$", message = "Format: example@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include uppercase, lowercase, numbers, and special characters"
    )
    private String password;

    // --- Added WTMS Specific Fields ---

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotNull(message = "Department ID is required")
    private Integer departmentId;
}