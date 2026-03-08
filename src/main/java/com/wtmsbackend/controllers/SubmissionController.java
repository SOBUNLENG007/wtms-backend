package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.GradeRequest;
import com.wtmsbackend.dto.request.SubmissionRequest;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.dto.response.SubmissionResponse;
import com.wtmsbackend.services.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "Get all submissions", description = "Retrieves a paginated list of all homework submissions.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> getAllSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getAllSubmissions(page, size);
        return ResponseEntity.ok(ApiResponse.ok("Submissions fetched successfully", buildPagedResponse(submissionPage)));
    }

    @Operation(summary = "Get submissions by Assignment", description = "Retrieves all submissions turned in for a specific assignment.")
    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> getSubmissionsByAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getSubmissionsByAssignment(assignmentId, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Assignment submissions fetched successfully", buildPagedResponse(submissionPage)));
    }

    @Operation(summary = "Get submissions by Employee", description = "Retrieves all submissions made by a specific employee.")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getSubmissionsByEmployee(
            @PathVariable Integer employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getSubmissionsByEmployee(employeeId, page, size);
        return ResponseEntity.ok(ApiResponse.ok("Employee submissions fetched successfully", buildPagedResponse(submissionPage)));
    }

    @Operation(summary = "Get submission by ID", description = "Retrieves specific submission details.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSubmissionById(@PathVariable Integer id) {
        SubmissionResponse submission = submissionService.getSubmissionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Submission fetched successfully", submission));
    }

    @Operation(summary = "Submit an assignment", description = "Uploads a completed assignment. Available to Employees.")
    @PostMapping
    public ResponseEntity<?> createSubmission(@Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse submission = submissionService.createSubmission(request);
        return ResponseEntity.ok(ApiResponse.ok("Assignment submitted successfully", submission));
    }

    @Operation(summary = "Update submitted file", description = "Updates the file URL of an existing submission.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSubmissionFile(
            @PathVariable Integer id,
            @Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse submission = submissionService.updateSubmissionFile(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Submission updated successfully", submission));
    }

    @Operation(summary = "Grade a submission", description = "Allows a Trainer or Admin to grade and leave feedback on a submission.")
    @PutMapping("/{id}/grade")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> gradeSubmission(
            @PathVariable Integer id,
            @Valid @RequestBody GradeRequest request) {
        SubmissionResponse submission = submissionService.gradeSubmission(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Submission graded successfully", submission));
    }

    @Operation(summary = "Delete a submission", description = "Permanently deletes a submission.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> deleteSubmission(@PathVariable Integer id) {
        submissionService.deleteSubmission(id);
        return ResponseEntity.ok(ApiResponse.ok("Submission deleted successfully", null));
    }

    private PagedResponse<SubmissionResponse> buildPagedResponse(Page<SubmissionResponse> page) {
        return PagedResponse.<SubmissionResponse>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}