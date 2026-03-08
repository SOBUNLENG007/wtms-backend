package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.AttendanceRequest;
import com.wtmsbackend.dto.response.AttendanceResponse;
import org.springframework.data.domain.Page;

public interface AttendanceService {
    Page<AttendanceResponse> getAllAttendance(int page, int size);
    Page<AttendanceResponse> getAttendanceBySession(Integer sessionId, int page, int size);
    Page<AttendanceResponse> getAttendanceByUser(Integer userId, int page, int size);
    AttendanceResponse getAttendanceById(Integer id);
    AttendanceResponse createAttendance(AttendanceRequest request);
    AttendanceResponse updateAttendance(Integer id, AttendanceRequest request);
    void deleteAttendance(Integer id);
}