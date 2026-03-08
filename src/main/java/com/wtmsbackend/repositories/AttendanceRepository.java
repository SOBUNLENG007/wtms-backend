package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Page<Attendance> findAll(Pageable pageable);

    // Get the attendance sheet for a specific session
    Page<Attendance> findBySessionId(Integer sessionId, Pageable pageable);

    // Get all attendance records for a specific employee
    Page<Attendance> findByUserId(Integer userId, Pageable pageable);
}