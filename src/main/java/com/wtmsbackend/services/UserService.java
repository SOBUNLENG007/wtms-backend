package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import org.springframework.data.domain.Page;


public interface UserService {
    Page<UserResponse> getAllUsers(int page, int size);
    UserResponse getUserById(Integer id);
    UserResponse getUserByEmail(String email);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Integer id, UserUpdateRequest request);
    void deleteUser(Integer id);
    void adminResetPassword(Integer id, String newPassword);
    // Add this new signature
    Page<UserResponse> getUsersByDepartment(Integer departmentId, int page, int size);
}