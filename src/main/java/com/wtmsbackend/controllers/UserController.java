package com.wtmsbackend.controllers;

import com.wtmsbackend.dto.ApiResponse;
import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.request.UserUpdateRequest;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.User;
import com.wtmsbackend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    // 1. Get all users with Pagination (ADMIN ONLY)
    @Operation(
            summary = "Get all users (Paginated)",
            description = "Retrieves a paginated list of all users. This endpoint is restricted to users with the ADMIN role."
    )
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponse> users = userService.getAllUsers(page, size);

        ApiResponse<Page<UserResponse>> response = ApiResponse.<Page<UserResponse>>builder()
                .message("Users fetched successfully")
                .success(true)
                .payload(users)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // 2. Get current logged-in user
    @Operation(
            summary = "Get current authenticated user",
            description = "Retrieves the details of the currently logged-in user based on the provided JWT Bearer token."
    )
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        UserResponse userResponse = UserResponse.builder()
                .id(currentUser.getId())
                .firstName(currentUser.getFirstName())
                .lastName(currentUser.getLastName())
                .email(currentUser.getEmail())
//                .phoneNumber(currentUser.getPhoneNumber())
                .address(currentUser.getAddress())
                .status(currentUser.getStatus())
                .build();

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("Current user fetched successfully")
                .success(true)
                .payload(userResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    // 3. Get user by ID
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user's details by their unique ID."
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User fetched successfully")
                .success(true)
                .payload(user)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // 4. Get user by Email
    @Operation(
            summary = "Get user by Email",
            description = "Retrieves a specific user's details by their registered email address."
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        UserResponse user = userService.getUserByEmail(email);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User fetched successfully")
                .success(true)
                .payload(user)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // 5. Create user
    @Operation(
            summary = "Create a new user",
            description = "Registers a new user in the system with the provided details."
    )
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequest request) {
        UserResponse user = userService.createUser(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User created successfully")
                .success(true)
                .payload(user)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // 6. Update user (No password update)
    @Operation(
            summary = "Update user details",
            description = "Updates an existing user's profile information. Note: This endpoint does not update the user's password."
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserUpdateRequest request) {

        UserResponse user = userService.updateUser(id, request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .message("User updated successfully")
                .success(true)
                .payload(user)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // 7. Soft Delete User
    @Operation(
            summary = "Deactivate (Soft Delete) user",
            description = "Deactivates a user by setting their status to false instead of permanently removing them from the database."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("User deleted (deactivated) successfully")
                .success(true)
                .payload(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(response);
    }

    // 8. Admin Reset Password by ID
    @Operation(
            summary = "Admin reset user password",
            description = "Allows an Admin to directly override and reset a specific user's password using their ID. Restricted to ADMIN role."
    )
    @PutMapping("/{id}/reset-password")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> userResetPassword(
            String newPassword,
            String oldPassword,
            @Valid @RequestBody com.wtmsbackend.dto.request.UserResetPasswordRequest request) {

        userService.userResetPassword(newPassword, oldPassword, request.getNewPassword());

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .message("User password reset successfully by Admin")
                .success(true)
                .payload(null)
                .timestamp(java.time.LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

//    @Operation(
//            summary = "Get users by Department ID",
//            description = "Retrieves a paginated list of users that belong to a specific department."
//    )
//    @GetMapping("/department/{departmentId}")
//    public ResponseEntity<?> getUsersByDepartment(
//            @PathVariable Integer departmentId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        Page<UserResponse> users = userService.getUsersByDepartment(departmentId, page, size);
//
//        ApiResponse<Page<UserResponse>> response = ApiResponse.<Page<UserResponse>>builder()
//                .message("Users fetched successfully by department")
//                .success(true)
//                .payload(users)
//                .timestamp(LocalDateTime.now())
//                .build();
//
//        return ResponseEntity.ok(response);
//    }

    @Operation(
            summary = "Get users by Department ID",
            description = "Retrieves a list of users that belong to a specific department."
    )
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getUsersByDepartment(
            @PathVariable Integer departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // Fetch the page from the service, but extract ONLY the list of UserResponse objects
        List<UserResponse> users = userService.getUsersByDepartment(departmentId, page, size).getContent();

        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .message("Users fetched successfully by department")
                .success(true)
                .payload(users)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}