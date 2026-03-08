package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.MaterialRequest;
import com.wtmsbackend.dto.response.MaterialResponse;
import com.wtmsbackend.dto.response.PagedResponse;
import com.wtmsbackend.services.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "Get all materials", description = "Retrieves a paginated list of all training materials in the system.")
    @GetMapping
    public ResponseEntity<?> getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MaterialResponse> materialPage = materialService.getAllMaterials(page, size);

        PagedResponse<MaterialResponse> pagedData = PagedResponse.<MaterialResponse>builder()
                .content(materialPage.getContent())
                .pageNumber(materialPage.getNumber())
                .pageSize(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .last(materialPage.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Materials fetched successfully", pagedData));
    }

    @Operation(summary = "Get materials by Session ID", description = "Retrieves a paginated list of materials assigned to a specific training session.")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<?> getMaterialsBySession(
            @PathVariable Integer sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<MaterialResponse> materialPage = materialService.getMaterialsBySession(sessionId, page, size);

        PagedResponse<MaterialResponse> pagedData = PagedResponse.<MaterialResponse>builder()
                .content(materialPage.getContent())
                .pageNumber(materialPage.getNumber())
                .pageSize(materialPage.getSize())
                .totalElements(materialPage.getTotalElements())
                .totalPages(materialPage.getTotalPages())
                .last(materialPage.isLast())
                .build();

        return ResponseEntity.ok(ApiResponse.ok("Materials for session fetched successfully", pagedData));
    }

    @Operation(summary = "Get material by ID", description = "Retrieves specific material details using its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaterialById(@PathVariable Integer id) {
        MaterialResponse material = materialService.getMaterialById(id);
        return ResponseEntity.ok(ApiResponse.ok("Material fetched successfully", material));
    }

    @Operation(summary = "Create a new material", description = "Uploads/Creates a new training material. Requires ADMIN or TRAINER role.")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> createMaterial(@Valid @RequestBody MaterialRequest request) {
        MaterialResponse material = materialService.createMaterial(request);
        return ResponseEntity.ok(ApiResponse.ok("Material created successfully", material));
    }

    @Operation(summary = "Update a material", description = "Updates details of an existing material. Requires ADMIN or TRAINER role.")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> updateMaterial(
            @PathVariable Integer id,
            @Valid @RequestBody MaterialRequest request) {
        MaterialResponse material = materialService.updateMaterial(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Material updated successfully", material));
    }

    @Operation(summary = "Delete a material", description = "Permanently deletes a material from the database. Requires ADMIN or TRAINER role.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    public ResponseEntity<?> deleteMaterial(@PathVariable Integer id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok(ApiResponse.ok("Material deleted successfully", null));
    }
}