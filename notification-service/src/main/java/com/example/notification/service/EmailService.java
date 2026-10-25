package com.example.notification.service;

import com.example.notification.dto.EmailRequest;
import com.example.notification.dto.EmailResponse;
import com.example.notification.dto.WelcomeEmailRequest;
import com.example.notification.model.NotificationLog;

import java.util.List;

public interface EmailService {

    EmailResponse sendEmail(EmailRequest request);

    EmailResponse sendWelcomeEmail(WelcomeEmailRequest request);

    List<NotificationLog> getNotificationHistory();
}
