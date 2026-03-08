package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.DepartmentRequest;
import com.wtmsbackend.dto.response.DepartmentResponse;
import com.wtmsbackend.services.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Requires JWT Token
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(
            summary = "Get all departments",
            description = "Retrieves a list of all departments in the system."
    )
    @GetMapping
    public ResponseEntity<?> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();

        ApiResponse<List<DepartmentResponse>> response = ApiResponse.<List<DepartmentResponse>>builder()
                .message("Departments fetched successfully")
                .success(true)
                .payload(departments)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get department by ID",
            description = "Retrieves specific department details using its unique ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Integer id) {
        DepartmentResponse department = departmentService.getDepartmentById(id);

        ApiResponse<DepartmentResponse> response = ApiResponse.<DepartmentResponse>builder()
                .message("Department fetched successfully")
                .success(true)
                .payload(department)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Create a new department",
            description = "Creates a new department. The department name must be unique."
    )
    @PostMapping
    public ResponseEntity<?> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        DepartmentResponse department = departmentService.createDepartment(request);

        ApiResponse<DepartmentResponse> response = ApiResponse.<DepartmentResponse>builder()
                .message("Department created successfully")
                .success(true)
                .payload(department)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update a department",
            description = "Updates the name and description of an existing department."
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody DepartmentRequest request) {

        DepartmentResponse department = departmentService.updateDepartment(id, request);

        ApiResponse<DepartmentResponse> response = ApiResponse.<DepartmentResponse>builder()
                .message("Department updated successfully")
                .success(true)
                .payload(department)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Deactivate (Soft Delete) a department",
            description = "Deactivates a department by setting its status to false."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Department deleted (deactivated) successfully")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}