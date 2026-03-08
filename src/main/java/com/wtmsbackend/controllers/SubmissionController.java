package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.GradeRequest;
import com.wtmsbackend.dto.request.SubmissionRequest;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.dto.response.SubmissionResponse;
import com.wtmsbackend.services.SubmissionService;
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
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Submissions", description = "Endpoints for managing assignment submissions and grading")
public class SubmissionController {

    private final SubmissionService submissionService;

    @Operation(summary = "Get all submissions", description = "Retrieves a paginated list of all homework submissions.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<PagedResponse<SubmissionResponse>>> getAllSubmissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getAllSubmissions(page, size);

        ApiResponse<PagedResponse<SubmissionResponse>> response = ApiResponse.<PagedResponse<SubmissionResponse>>builder()
                .message("Submissions fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(submissionPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get submissions by Assignment", description = "Retrieves all submissions turned in for a specific assignment.")
    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<PagedResponse<SubmissionResponse>>> getSubmissionsByAssignment(
            @PathVariable Integer assignmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getSubmissionsByAssignment(assignmentId, page, size);

        ApiResponse<PagedResponse<SubmissionResponse>> response = ApiResponse.<PagedResponse<SubmissionResponse>>builder()
                .message("Assignment submissions fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(submissionPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get submissions by Employee", description = "Retrieves all submissions made by a specific employee.")
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<PagedResponse<SubmissionResponse>>> getSubmissionsByEmployee(
            @PathVariable Integer employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SubmissionResponse> submissionPage = submissionService.getSubmissionsByEmployee(employeeId, page, size);

        ApiResponse<PagedResponse<SubmissionResponse>> response = ApiResponse.<PagedResponse<SubmissionResponse>>builder()
                .message("Employee submissions fetched successfully!")
                .success(true)
                .payload(buildPagedResponse(submissionPage))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get submission by ID", description = "Retrieves specific submission details.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> getSubmissionById(@PathVariable Integer id) {
        SubmissionResponse submission = submissionService.getSubmissionById(id);

        ApiResponse<SubmissionResponse> response = ApiResponse.<SubmissionResponse>builder()
                .message("Submission fetched successfully!")
                .success(true)
                .payload(submission)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Submit an assignment", description = "Uploads a completed assignment. Available to Employees.")
    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> createSubmission(@Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse submission = submissionService.createSubmission(request);

        ApiResponse<SubmissionResponse> response = ApiResponse.<SubmissionResponse>builder()
                .message("Assignment submitted successfully!")
                .success(true)
                .payload(submission)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update submitted file", description = "Updates the file URL of an existing submission.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubmissionResponse>> updateSubmissionFile(
            @PathVariable Integer id,
            @Valid @RequestBody SubmissionRequest request) {
        SubmissionResponse submission = submissionService.updateSubmissionFile(id, request);

        ApiResponse<SubmissionResponse> response = ApiResponse.<SubmissionResponse>builder()
                .message("Submission updated successfully!")
                .success(true)
                .payload(submission)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Grade a submission", description = "Allows a Trainer or Admin to grade and leave feedback on a submission.")
    @PutMapping("/{id}/grade")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<SubmissionResponse>> gradeSubmission(
            @PathVariable Integer id,
            @Valid @RequestBody GradeRequest request) {
        SubmissionResponse submission = submissionService.gradeSubmission(id, request);

        ApiResponse<SubmissionResponse> response = ApiResponse.<SubmissionResponse>builder()
                .message("Submission graded successfully!")
                .success(true)
                .payload(submission)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a submission", description = "Permanently deletes a submission.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<ApiResponse<Void>> deleteSubmission(@PathVariable Integer id) {
        submissionService.deleteSubmission(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Submission deleted successfully!")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
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