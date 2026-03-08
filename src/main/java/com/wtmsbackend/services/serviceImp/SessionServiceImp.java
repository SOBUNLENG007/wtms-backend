package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.SessionRequest;
import com.wtmsbackend.dto.response.SessionResponse;
import com.wtmsbackend.models.Department;
import com.wtmsbackend.models.Session;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.DepartmentRepository;
import com.wtmsbackend.repositories.SessionRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImp implements SessionService {

    private final SessionRepository sessionRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public Page<SessionResponse> getAllSessions(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return sessionRepository.findAll(pageRequest).map(this::mapToResponse);
    }

    @Override
    public SessionResponse getSessionById(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));
        return mapToResponse(session);
    }

    @Override
    public SessionResponse createSession(SessionRequest request) {
        // Verify Department exists
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + request.getDepartmentId()));

        // Verify Instructor exists
        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor (User) not found with ID: " + request.getInstructorId()));

        Session session = Session.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .numSession(request.getNumSession())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .department(department)
                .instructor(instructor)
                .status(true)
                .build();

        return mapToResponse(sessionRepository.save(session));
    }

    @Override
    public SessionResponse updateSession(Integer id, SessionRequest request) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

        // Update basic fields
        session.setTitle(request.getTitle());
        session.setDescription(request.getDescription());
        session.setNumSession(request.getNumSession());
        session.setStartDate(request.getStartDate());
        session.setEndDate(request.getEndDate());

        // Update relationships if they changed
        if (!session.getDepartment().getId().equals(request.getDepartmentId())) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            session.setDepartment(department);
        }

        if (!session.getInstructor().getId().equals(request.getInstructorId())) {
            User instructor = userRepository.findById(request.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found"));
            session.setInstructor(instructor);
        }

        return mapToResponse(sessionRepository.save(session));
    }

    @Override
    public void deleteSession(Integer id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with ID: " + id));

        // Soft delete
        session.setStatus(false);
        sessionRepository.save(session);
    }

    // Helper Mapper
    private SessionResponse mapToResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .title(session.getTitle())
                .description(session.getDescription())
                .numSession(session.getNumSession())
                .startDate(session.getStartDate())
                .endDate(session.getEndDate())
                .status(session.getStatus())
                .departmentId(session.getDepartment() != null ? session.getDepartment().getId() : null)
                .departmentName(session.getDepartment() != null ? session.getDepartment().getName() : null)
                .instructorId(session.getInstructor() != null ? session.getInstructor().getId() : null)
                .instructorName(session.getInstructor() != null ?
                        (session.getInstructor().getFirstName() + " " + session.getInstructor().getLastName()) : null)
                .build();
    }
}