package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    Page<Submission> findAll(Pageable pageable);

    // For Trainers: See everyone who turned in a specific homework
    Page<Submission> findByAssignmentId(Integer assignmentId, Pageable pageable);

    // For Employees: See their own homework history and grades
    Page<Submission> findByEmployeeId(Integer employeeId, Pageable pageable);
}