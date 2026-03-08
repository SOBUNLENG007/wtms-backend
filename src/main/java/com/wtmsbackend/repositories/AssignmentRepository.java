package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    Page<Assignment> findAll(Pageable pageable);

    // Fetch all assignments for a specific session
    Page<Assignment> findBySessionId(Integer sessionId, Pageable pageable);
}