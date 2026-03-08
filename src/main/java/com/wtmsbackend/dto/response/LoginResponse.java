package com.wtmsbackend.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@NotBlank
@Builder
public class LoginResponse {
    private UserResponse user;
    private String accessToken;
    private String refreshToken;
}
