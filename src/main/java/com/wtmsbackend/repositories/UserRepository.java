package com.wtmsbackend.repositories;

import com.wtmsbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findByDepartmentId(Integer departmentId, Pageable pageable);
}