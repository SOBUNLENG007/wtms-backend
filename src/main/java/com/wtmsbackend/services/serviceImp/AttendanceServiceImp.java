package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.AttendanceRequest;
import com.wtmsbackend.dto.response.AttendanceResponse;
import com.wtmsbackend.models.Attendance;
import com.wtmsbackend.models.Session;
import com.wtmsbackend.models.User;
import com.wtmsbackend.repositories.AttendanceRepository;
import com.wtmsbackend.repositories.SessionRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.services.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImp implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public Page<AttendanceResponse> getAllAttendance(int page, int size) {
        return attendanceRepository.findAll(PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public Page<AttendanceResponse> getAttendanceBySession(Integer sessionId, int page, int size) {
        return attendanceRepository.findBySessionId(sessionId, PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public Page<AttendanceResponse> getAttendanceByUser(Integer userId, int page, int size) {
        return attendanceRepository.findByUserId(userId, PageRequest.of(page, size)).map(this::mapToResponse);
    }

    @Override
    public AttendanceResponse getAttendanceById(Integer id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found with ID: " + id));
        return mapToResponse(attendance);
    }

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {
        User trainee = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session not found"));
        User trainer = userRepository.findById(request.getMarkedById())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        Attendance attendance = Attendance.builder()
                .user(trainee)
                .session(session)
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus().toUpperCase())
                .markedBy(trainer)
                .build();

        return mapToResponse(attendanceRepository.save(attendance));
    }

    @Override
    public AttendanceResponse updateAttendance(Integer id, AttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));

        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus().toUpperCase());

        // Update relationships if necessary
        if (!attendance.getUser().getId().equals(request.getUserId())) {
            attendance.setUser(userRepository.findById(request.getUserId()).orElseThrow());
        }
        if (!attendance.getSession().getId().equals(request.getSessionId())) {
            attendance.setSession(sessionRepository.findById(request.getSessionId()).orElseThrow());
        }
        if (!attendance.getMarkedBy().getId().equals(request.getMarkedById())) {
            attendance.setMarkedBy(userRepository.findById(request.getMarkedById()).orElseThrow());
        }

        return mapToResponse(attendanceRepository.save(attendance));
    }

    @Override
    public void deleteAttendance(Integer id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance record not found"));
        attendanceRepository.delete(attendance);
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .userId(attendance.getUser().getId())
                .userName(attendance.getUser().getFirstName() + " " + attendance.getUser().getLastName())
                .sessionId(attendance.getSession().getId())
                .sessionTitle(attendance.getSession().getTitle())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .markedById(attendance.getMarkedBy().getId())
                .markedByName(attendance.getMarkedBy().getFirstName() + " " + attendance.getMarkedBy().getLastName())
                .build();
    }
}