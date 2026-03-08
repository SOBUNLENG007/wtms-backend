package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.AssignmentRequest;
import com.wtmsbackend.dto.response.AssignmentResponse;
import com.wtmsbackend.models.Assignment;
import com.wtmsbackend.models.Session;
import com.wtmsbackend.repositories.AssignmentRepository;
import com.wtmsbackend.repositories.SessionRepository;
import com.wtmsbackend.services.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImp implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Page<AssignmentResponse> getAllAssignments(int page, int size) {
        return assignmentRepository.findAll(PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public Page<AssignmentResponse> getAssignmentsBySession(Integer sessionId, int page, int size) {
        return assignmentRepository.findBySessionId(sessionId, PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public AssignmentResponse getAssignmentById(Integer id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));
        return mapToResponse(assignment);
    }

    @Override
    public AssignmentResponse createAssignment(AssignmentRequest request) {
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + request.getSessionId()));

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .totalScore(request.getTotalScore())
                .session(session)
                .build();

        return mapToResponse(assignmentRepository.save(assignment));
    }

    @Override
    public AssignmentResponse updateAssignment(Integer id, AssignmentRequest request) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));

        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setDeadline(request.getDeadline());
        assignment.setTotalScore(request.getTotalScore());

        if (!assignment.getSession().getId().equals(request.getSessionId())) {
            Session session = sessionRepository.findById(request.getSessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            assignment.setSession(session);
        }

        return mapToResponse(assignmentRepository.save(assignment));
    }

    @Override
    public void deleteAssignment(Integer id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found with ID: " + id));
        assignmentRepository.delete(assignment);
    }

    private AssignmentResponse mapToResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .deadline(assignment.getDeadline())
                .totalScore(assignment.getTotalScore())
                .sessionId(assignment.getSession().getId())
                .sessionTitle(assignment.getSession().getTitle())
                .createdAt(assignment.getCreatedAt())
                .build();
    }
}