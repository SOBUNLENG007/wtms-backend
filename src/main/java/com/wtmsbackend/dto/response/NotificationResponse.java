package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Integer id;
    private Integer userId;
    private String userName;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}