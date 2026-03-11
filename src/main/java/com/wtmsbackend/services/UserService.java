package com.wtmsbackend.services;

import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;


public interface UserService {
    Page<UserResponse> getAllUsers(int page, int size);
    UserResponse getUserById(Integer id);
    UserResponse getUserByEmail(String email);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Integer id, UserUpdateRequest request);
    void deleteUser(Integer id);
    // Add this new signature
    Page<UserResponse> getUsersByDepartment(Integer departmentId, int page, int size);

    void userResetPassword(String newPassword, String oldPassword, @NotBlank(message = "New password is required") @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and include uppercase, lowercase, numbers, and special characters"
    ) String newPassword1);
}