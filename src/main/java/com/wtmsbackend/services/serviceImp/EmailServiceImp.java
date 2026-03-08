package com.wtmsbackend.services.serviceImp;

import com.wtmsbackend.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendOtpEmail(String toEmail, String otpCode) throws MessagingException {
        Context context = new Context();
        context.setVariable("code", otpCode);

        String processedString = templateEngine.process("otp-email", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("so.bunleng246800@gmail.com"); // Change to your sender email
        helper.setTo(toEmail);
        helper.setSubject("WTMS - Account Verification Code");
        helper.setText(processedString, true);

        javaMailSender.send(message);
    }
}