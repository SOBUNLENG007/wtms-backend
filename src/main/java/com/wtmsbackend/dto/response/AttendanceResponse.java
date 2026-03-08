package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AttendanceResponse {
    private Integer id;
    private Integer userId;
    private String userName;
    private Integer sessionId;
    private String sessionTitle;
    private LocalDate attendanceDate;
    private String status;
    private Integer markedById;
    private String markedByName;
}