package com.wtmsbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name cannot exceed 100 characters") // Matches DB VARCHAR(100)
    private String name;

    @NotBlank(message = "Department description is required") // DB says TEXT NOT NULL
    @Size(max = 2000, message = "Department description is too long") // Safety net for TEXT columns
    private String description;
}