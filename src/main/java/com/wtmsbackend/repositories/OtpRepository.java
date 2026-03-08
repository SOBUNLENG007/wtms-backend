package com.wtmsbackend.repositories;

import com.wtmsbackend.models.Otp;
import com.wtmsbackend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findByOtpCodeAndUser(String otpCode, User user);
    Optional<Otp> findTopByUserOrderByCreatedAtDesc(User user); // Gets the latest OTP for a user
}