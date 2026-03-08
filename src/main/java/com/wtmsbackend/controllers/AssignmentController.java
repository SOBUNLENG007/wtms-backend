package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.AssignmentRequest;
import com.wtmsbackend.dto.response.AssignmentResponse;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.services.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Operation(summary = "Get all assignments", description = "Retrieves a paginated list of all assignments across all sessions.")
    @GetMapping
    public ResponseEntity<?> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AssignmentResponse> assignmentPage = assignmentService.getAllAssignments(page, size);
        PagedResponse<AssignmentResponse> pagedData = buildPagedResponse(assignmentPage);
        return ResponseEntity.ok(ApiResponse.ok("Assignments fetched successfully", pagedData));
    }

    @Operation(summary = "Get assignments by Session ID", description = "Retrieves a paginated list of assignments belonging to a specific session.")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getAssignmentsBySession(
            @PathVariable Integer sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AssignmentResponse> assignmentPage = assignmentService.getAssignmentsBySession(sessionId, page, size);
        PagedResponse<AssignmentResponse> pagedData = buildPagedResponse(assignmentPage);
        return ResponseEntity.ok(ApiResponse.ok("Session assignments fetched successfully", pagedData));
    }

    @Operation(summary = "Get assignment by ID", description = "Retrieves specific assignment details.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Integer id) {
        AssignmentResponse assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(ApiResponse.ok("Assignment fetched successfully", assignment));
    }

    @Operation(summary = "Create a new assignment", description = "Creates a new assignment for a session. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> createAssignment(@Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignment = assignmentService.createAssignment(request);
        return ResponseEntity.ok(ApiResponse.ok("Assignment created successfully", assignment));
    }

    @Operation(summary = "Update an assignment", description = "Updates details of an existing assignment. Requires ADMIN or TRAINER role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Integer id,
            @Valid @RequestBody AssignmentRequest request) {
        AssignmentResponse assignment = assignmentService.updateAssignment(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Assignment updated successfully", assignment));
    }

    @Operation(summary = "Delete an assignment", description = "Permanently deletes an assignment. Requires ADMIN or TRAINER role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok(ApiResponse.ok("Assignment deleted successfully", null));
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