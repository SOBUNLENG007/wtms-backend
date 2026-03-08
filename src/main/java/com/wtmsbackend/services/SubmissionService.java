package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.GradeRequest;
import com.wtmsbackend.dto.request.SubmissionRequest;
import com.wtmsbackend.dto.response.SubmissionResponse;
import org.springframework.data.domain.Page;

public interface SubmissionService {
    Page<SubmissionResponse> getAllSubmissions(int page, int size);
    Page<SubmissionResponse> getSubmissionsByAssignment(Integer assignmentId, int page, int size);
    Page<SubmissionResponse> getSubmissionsByEmployee(Integer employeeId, int page, int size);
    SubmissionResponse getSubmissionById(Integer id);

    // Employee actions
    SubmissionResponse createSubmission(SubmissionRequest request);
    SubmissionResponse updateSubmissionFile(Integer id, SubmissionRequest request);

    // Trainer actions
    SubmissionResponse gradeSubmission(Integer id, GradeRequest request);

    void deleteSubmission(Integer id);
}