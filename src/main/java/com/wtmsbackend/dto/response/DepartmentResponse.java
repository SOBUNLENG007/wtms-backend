package com.wtmsbackend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private Integer id;
    private String name;
    private String description;
    private Boolean status;
}