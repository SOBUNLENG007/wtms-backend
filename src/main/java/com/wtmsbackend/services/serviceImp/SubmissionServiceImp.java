package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.GradeRequest;
import com.wtmsbackend.dto.request.SubmissionRequest;
import com.wtmsbackend.dto.response.SubmissionResponse;
import com.wtmsbackend.models.Assignment;
import com.wtmsbackend.models.Submission;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.AssignmentRepository;
import com.wtmsbackend.repositories.SubmissionRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImp implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    public Page<SubmissionResponse> getAllSubmissions(int page, int size) {
        return submissionRepository.findAll(PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public Page<SubmissionResponse> getSubmissionsByAssignment(Integer assignmentId, int page, int size) {
        return submissionRepository.findByAssignmentId(assignmentId, PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public Page<SubmissionResponse> getSubmissionsByEmployee(Integer employeeId, int page, int size) {
        return submissionRepository.findByEmployeeId(employeeId, PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public SubmissionResponse getSubmissionById(Integer id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found with ID: " + id));
        return mapToResponse(submission);
    }

    // 1. Employee Submits Homework
    @Override
    public SubmissionResponse createSubmission(SubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User employee = userRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Submission submission = Submission.builder()
                .assignment(assignment)
                .employee(employee)
                .fileUrl(request.getFileUrl())
                // Score and feedback stay null until the trainer grades it!
                .build();

        return mapToResponse(submissionRepository.save(submission));
    }

    // 2. Employee changes their file before grading
    @Override
    public SubmissionResponse updateSubmissionFile(Integer id, SubmissionRequest request) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setFileUrl(request.getFileUrl());
        return mapToResponse(submissionRepository.save(submission));
    }

    // 3. Trainer grades the homework
    @Override
    public SubmissionResponse gradeSubmission(Integer id, GradeRequest request) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        // Prevent grading higher than the max score
        if (request.getScore() > submission.getAssignment().getTotalScore()) {
            throw new RuntimeException("Score cannot be higher than the assignment's max score of " + submission.getAssignment().getTotalScore());
        }

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());

        return mapToResponse(submissionRepository.save(submission));
    }

    @Override
    public void deleteSubmission(Integer id) {
        Submission submission = submissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submissionRepository.delete(submission);
    }

    private SubmissionResponse mapToResponse(Submission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .assignmentId(submission.getAssignment().getId())
                .assignmentTitle(submission.getAssignment().getTitle())
                .employeeId(submission.getEmployee().getId())
                .employeeName(submission.getEmployee().getFirstName() + " " + submission.getEmployee().getLastName())
                .fileUrl(submission.getFileUrl())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .createdAt(submission.getCreatedAt())
                .updatedAt(submission.getUpdatedAt())
                .build();
    }
}