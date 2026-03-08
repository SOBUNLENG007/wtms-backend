package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.MaterialRequest;
import com.wtmsbackend.dto.response.MaterialResponse;
import org.springframework.data.domain.Page;

public interface MaterialService {
    Page<MaterialResponse> getAllMaterials(int page, int size);
    Page<MaterialResponse> getMaterialsBySession(Integer sessionId, int page, int size);
    MaterialResponse getMaterialById(Integer id);
    MaterialResponse createMaterial(MaterialRequest request);
    MaterialResponse updateMaterial(Integer id, MaterialRequest request);
    void deleteMaterial(Integer id);
}