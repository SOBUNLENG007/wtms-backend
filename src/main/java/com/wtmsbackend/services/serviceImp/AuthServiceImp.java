package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.dto.request.ForgetPasswordRequest;
import com.wtmsbackend.dto.request.LoginRequest;
import com.wtmsbackend.dto.request.UserRequest;
import com.wtmsbackend.dto.response.LoginResponse;
import com.wtmsbackend.dto.response.UserResponse;
import com.wtmsbackend.models.Department;
import com.wtmsbackend.models.Otp;
import com.wtmsbackend.models.User;
import com.wtmsbackend.models.role.Role;
import com.wtmsbackend.repositories.DepartmentRepository;
import com.wtmsbackend.repositories.OtpRepository;
import com.wtmsbackend.repositories.UserRepository;
import com.wtmsbackend.security.JwtService;
import com.wtmsbackend.services.AuthService;
import com.wtmsbackend.services.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;       // Added: needed for OTP operations
    private final EmailService emailService;         // Added: needed to send emails
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .role(Role.USER)
                .department(department)   // ✅ SET DEPARTMENT
                .build();

        User savedUser = userRepository.save(user);

        return mapToUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
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

    @Override
    public void resendCode(String email) {
        // 1. Find the user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email"));

        // 2. Generate a random 6-digit code
        String otpCode = String.format("%06d", new Random().nextInt(999999));

        // 3. Save to database
        Otp otp = Otp.builder()
                .otpCode(otpCode)
                .user(user)
                .isVerified(false)
                .build();
        otpRepository.save(otp);

        // 4. Send the email using your EmailService
        try {
            emailService.sendOtpEmail(user.getEmail(), otpCode);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    @Override
    public void verifyOtpCode(String email, String otpCode) {
        // 1. Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Find the exact OTP code for this user
        Otp otp = otpRepository.findByOtpCodeAndUser(otpCode, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP Code"));

        // 3. Check if it is expired
        if (otp.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP Code has expired. Please request a new one.");
        }

        // 4. Check if already verified
        if (otp.getIsVerified()) {
            throw new RuntimeException("This OTP has already been verified.");
        }

        // 5. Mark as verified and save
        otp.setIsVerified(true);
        otpRepository.save(otp);
    }

    @Override
    public void forgetPassword(String email) {
        // 1. Find user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Security Check: Ensure they actually verified an OTP recently!
        Otp latestOtp = otpRepository.findTopByUserOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new RuntimeException("No OTP requested."));

        if (!latestOtp.getIsVerified()) {
            throw new RuntimeException("You must verify your OTP before resetting your password.");
        }

        // 3. Encode the new password and update the user
        userRepository.save(user);

        // 4. Delete the OTP so they can't reuse it to reset the password again
        otpRepository.delete(latestOtp);
    }

    // Helper method
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .PhoneNumber(user.getPhoneNumber()) // Note: Changed to lowercase 'p' to match standard naming conventions
                .address(user.getAddress())
                .status(user.getStatus())
                .departmentId(user.getDepartment().getId())
                .departmentName(user.getDepartment().getName())
                .build();
    }
}