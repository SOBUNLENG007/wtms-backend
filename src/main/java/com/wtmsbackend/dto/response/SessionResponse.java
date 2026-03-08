package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SessionResponse {
    private Integer id;
    private String title;
    private String description;
    private Integer numSession;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean status;

    // Relational Data (Returning Names/IDs instead of full nested objects to keep JSON clean)
    private Integer departmentId;
    private String departmentName;
    private Integer instructorId;
    private String instructorName;
}