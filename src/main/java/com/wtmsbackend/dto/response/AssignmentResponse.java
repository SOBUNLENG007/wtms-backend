package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssignmentResponse {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private Integer totalScore;
    private Integer sessionId;
    private String sessionTitle;
    private LocalDateTime createdAt;
}