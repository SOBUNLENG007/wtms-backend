package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.SessionRequest;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.dto.response.SessionResponse;
import com.wtmsbackend.services.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SessionController {

    private final SessionService sessionService;

    @Operation(summary = "Get all sessions (Paginated)", description = "Retrieves a paginated list of all training sessions.")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<SessionResponse>>> getAllSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SessionResponse> sessionPage = sessionService.getAllSessions(page, size);

        PagedResponse<SessionResponse> pagedData = PagedResponse.<SessionResponse>builder()
                .content(sessionPage.getContent())
                .pageNumber(sessionPage.getNumber())
                .pageSize(sessionPage.getSize())
                .totalElements(sessionPage.getTotalElements())
                .totalPages(sessionPage.getTotalPages())
                .last(sessionPage.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Sessions fetched successfully", pagedData));
    }

    @Operation(summary = "Get session by ID", description = "Retrieves specific session details using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> getSessionById(@PathVariable Integer id) {
        SessionResponse session = sessionService.getSessionById(id);
        return ResponseEntity.ok(ApiResponse.ok("Session fetched successfully", session));
    }

    @Operation(summary = "Create a new session", description = "Creates a new training session. Requires ADMIN authority.")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> createSession(@Valid @RequestBody SessionRequest request) {
        SessionResponse session = sessionService.createSession(request);
        return ResponseEntity.ok(ApiResponse.ok("Session created successfully", session));
    }

    @Operation(summary = "Update a session", description = "Updates details of an existing session. Requires ADMIN authority.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> updateSession(
            @PathVariable Integer id,
            @Valid @RequestBody SessionRequest request) {
        SessionResponse session = sessionService.updateSession(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Session updated successfully", session));
    }

    @Operation(summary = "Deactivate (Soft Delete) a session", description = "Deactivates a session by setting its status to false. Requires ADMIN authority.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Integer id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok(ApiResponse.ok("Session deleted (deactivated) successfully", null));
    }
}