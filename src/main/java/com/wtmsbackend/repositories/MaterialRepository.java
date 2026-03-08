package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Page<Material> findAll(Pageable pageable);

    // Fetch all materials that belong to a specific session
    Page<Material> findBySessionId(Integer sessionId, Pageable pageable);
}