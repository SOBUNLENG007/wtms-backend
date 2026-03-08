package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    // Pagination built-in
    Page<Session> findAll(Pageable pageable);

    // Optional: Useful for getting all sessions in a specific department
    Page<Session> findByDepartmentId(Integer departmentId, Pageable pageable);

    // Optional: Useful for an instructor to see their assigned sessions
    Page<Session> findByInstructorId(Integer instructorId, Pageable pageable);
}