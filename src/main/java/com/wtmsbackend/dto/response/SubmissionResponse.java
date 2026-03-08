package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SubmissionResponse {
    private Integer id;
    private Integer assignmentId;
    private String assignmentTitle;
    private Integer employeeId;
    private String employeeName;
    private String fileUrl;
    private Integer score;
    private String feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}