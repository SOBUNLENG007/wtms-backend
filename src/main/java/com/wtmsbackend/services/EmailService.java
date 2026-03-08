package com.wtmsbackend.services;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendOtpEmail(String toEmail, String otpCode) throws MessagingException;
}