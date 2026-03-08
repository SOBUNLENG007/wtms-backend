package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MaterialResponse {
    private Integer id;
    private String title;
    private String fileUrl;
    private Integer sessionId;
    private String sessionTitle;
    private Integer trainerId;
    private String trainerName;
    private LocalDateTime createdAt;
}