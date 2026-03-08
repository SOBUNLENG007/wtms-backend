package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.LoginRequest;
import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.response.LoginResponse;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.User;
import com.wtmsbackend.models.role.Role;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.security.JwtService;
import com.wtmsbackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword())) // Hashed password
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .role(Role.USER) // Default role
                .build();

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // This will verify the password using BCrypt and throw an exception if invalid
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .user(mapToUserResponse(user))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    // Helper method - if you are using MapStruct, you can replace this with your mapper!
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .PhoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

}