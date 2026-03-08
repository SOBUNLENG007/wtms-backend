package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.AssignmentRequest;
import com.wtmsbackend.dto.response.AssignmentResponse;
import org.springframework.data.domain.Page;

public interface AssignmentService {
    Page<AssignmentResponse> getAllAssignments(int page, int size);
    Page<AssignmentResponse> getAssignmentsBySession(Integer sessionId, int page, int size);
    AssignmentResponse getAssignmentById(Integer id);
    AssignmentResponse createAssignment(AssignmentRequest request);
    AssignmentResponse updateAssignment(Integer id, AssignmentRequest request);
    void deleteAssignment(Integer id);
}