package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.LoginRequest;
import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.response.LoginResponse;
import com.wtmsbackend.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(UserRequest userRequest);
    LoginResponse login(LoginRequest request);
}