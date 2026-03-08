package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.SessionRequest;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.dto.response.SessionResponse;
import com.wtmsbackend.services.SessionService;
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
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Sessions", description = "Endpoints for managing training sessions")
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

        ApiResponse<PagedResponse<SessionResponse>> response = ApiResponse.<PagedResponse<SessionResponse>>builder()
                .message("Sessions fetched successfully!")
                .success(true)
                .payload(pagedData)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get session by ID", description = "Retrieves specific session details using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> getSessionById(@PathVariable Integer id) {
        SessionResponse session = sessionService.getSessionById(id);

        ApiResponse<SessionResponse> response = ApiResponse.<SessionResponse>builder()
                .message("Session fetched successfully!")
                .success(true)
                .payload(session)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create a new session", description = "Creates a new training session. Requires ADMIN authority.")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> createSession(@Valid @RequestBody SessionRequest request) {
        SessionResponse session = sessionService.createSession(request);

        ApiResponse<SessionResponse> response = ApiResponse.<SessionResponse>builder()
                .message("Session created successfully!")
                .success(true)
                .payload(session)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a session", description = "Updates details of an existing session. Requires ADMIN authority.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> updateSession(
            @PathVariable Integer id,
            @Valid @RequestBody SessionRequest request) {
        SessionResponse session = sessionService.updateSession(id, request);

        ApiResponse<SessionResponse> response = ApiResponse.<SessionResponse>builder()
                .message("Session updated successfully!")
                .success(true)
                .payload(session)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Deactivate (Soft Delete) a session", description = "Deactivates a session by setting its status to false. Requires ADMIN authority.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Integer id) {
        sessionService.deleteSession(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("Session deleted (deactivated) successfully!")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}