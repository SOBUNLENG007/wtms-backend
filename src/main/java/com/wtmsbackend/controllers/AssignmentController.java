package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.AssignmentRequest;
import com.wtmsbackend.dto.response.AssignmentResponse;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.services.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Assignments", description = "Endpoints for managing training assignments and homework")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(summary = "Get all assignments", description = "Retrieves a paginated list of all assignments across all sessions.")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<AssignmentResponse>>> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AssignmentResponse> assignmentPage = assignmentService.getAllAssignments(page, size);

        ApiResponse<PagedResponse<AssignmentResponse>> response = ApiResponse.<PagedResponse<AssignmentResponse>>builder()
                .message("Assignments fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(assignmentPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get assignments by Session ID", description = "Retrieves a paginated list of assignments belonging to a specific session.")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<ApiResponse<PagedResponse<AssignmentResponse>>> getAssignmentsBySession(
            @PathVariable Integer sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AssignmentResponse> assignmentPage = assignmentService.getAssignmentsBySession(sessionId, page, size);

        ApiResponse<PagedResponse<AssignmentResponse>> response = ApiResponse.<PagedResponse<AssignmentResponse>>builder()
                .message("Session assignments fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(assignmentPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get assignment by ID", description = "Retrieves specific assignment details.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AssignmentResponse>> getAssignmentById(@PathVariable Integer id) {
        AssignmentResponse assignment = assignmentService.getAssignmentById(id);

        ApiResponse<AssignmentResponse> response = ApiResponse.<AssignmentResponse>builder()
                .message("Assignment fetched successfully!")
                .success(true)
                .payload(assignment)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new assignment", description = "Creates a new assignment for a session. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<AssignmentResponse>> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignment = assignmentService.createAssignment(request);

        ApiResponse<AssignmentResponse> response = ApiResponse.<AssignmentResponse>builder()
                .message("Assignment created successfully!")
                .success(true)
                .payload(assignment)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an assignment", description = "Updates details of an existing assignment. Requires ADMIN or TRAINER role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<AssignmentResponse>> updateAssignment(
            @PathVariable Integer id,
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignment = assignmentService.updateAssignment(id, request);

        ApiResponse<AssignmentResponse> response = ApiResponse.<AssignmentResponse>builder()
                .message("Assignment updated successfully!")
                .success(true)
                .payload(assignment)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an assignment", description = "Permanently deletes an assignment. Requires ADMIN or TRAINER role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<Void>> deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Assignment deleted successfully!")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // Helper method for clean pagination mapping
    private PagedResponse<AssignmentResponse> buildPagedResponse(Page<AssignmentResponse> page) {
        return PagedResponse.<AssignmentResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}