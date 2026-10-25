package com.example.notification.service.impl;

import com.example.notification.dto.EmailRequest;
import com.example.notification.dto.EmailResponse;
import com.example.notification.dto.WelcomeEmailRequest;
import com.example.notification.model.NotificationLog;
import com.example.notification.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${notification.mail.from:noreply@employeeportal.com}")
    private String defaultFromAddress;

    @Value("${notification.mail.simulate:auto}")
    private String simulateMode;

    // Thread-safe in-memory log buffer for tracking notifications
    private final List<NotificationLog> history = new CopyOnWriteArrayList<>();

    @Override
    public EmailResponse sendEmail(EmailRequest request) {
        String trackingId = UUID.randomUUID().toString();
        log.info("[NotificationService] Processing email trackingId={}, to={}, subject='{}', isHtml={}",
                trackingId, request.getTo(), request.getSubject(), request.isHtml());

        boolean shouldSimulate = isSimulateMode();

        if (shouldSimulate) {
            return simulateEmailDispatch(trackingId, request.getTo(), request.getCc(),
                    request.getSubject(), request.getBody(), request.isHtml() ? "HTML" : "TEXT");
        }

        try {
            if (request.isHtml()) {
                sendHtml(request.getTo(), request.getCc(), request.getSubject(), request.getBody());
            } else {
                sendPlain(request.getTo(), request.getCc(), request.getSubject(), request.getBody());
            }

            recordLog(trackingId, request.getTo(), request.getCc(), request.getSubject(),
                    request.isHtml() ? "HTML" : "TEXT", "SENT", null);

            return EmailResponse.builder()
                    .trackingId(trackingId)
                    .recipient(request.getTo())
                    .subject(request.getSubject())
                    .status("SENT")
                    .sentAt(LocalDateTime.now())
                    .message("Email sent successfully via SMTP")
                    .build();

        } catch (Exception e) {
            log.error("[NotificationService] SMTP delivery failed for trackingId={}: {}", trackingId, e.getMessage(), e);
            recordLog(trackingId, request.getTo(), request.getCc(), request.getSubject(),
                    request.isHtml() ? "HTML" : "TEXT", "FAILED", e.getMessage());

            return EmailResponse.builder()
                    .trackingId(trackingId)
                    .recipient(request.getTo())
                    .subject(request.getSubject())
                    .status("FAILED")
                    .sentAt(LocalDateTime.now())
                    .message("Failed to send email via SMTP: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public EmailResponse sendWelcomeEmail(WelcomeEmailRequest request) {
        String trackingId = UUID.randomUUID().toString();
        String subject = "🎉 Welcome to the Team, " + request.getEmployeeName() + "!";
        String htmlBody = buildWelcomeHtmlTemplate(request);

        log.info("[NotificationService] Dispatching onboarding welcome email trackingId={}, to={}", trackingId, request.getTo());

        boolean shouldSimulate = isSimulateMode();

        if (shouldSimulate) {
            return simulateEmailDispatch(trackingId, request.getTo(), null, subject, htmlBody, "WELCOME");
        }

        try {
            sendHtml(request.getTo(), null, subject, htmlBody);
            recordLog(trackingId, request.getTo(), null, subject, "WELCOME", "SENT", null);

            return EmailResponse.builder()
                    .trackingId(trackingId)
                    .recipient(request.getTo())
                    .subject(subject)
                    .status("SENT")
                    .sentAt(LocalDateTime.now())
                    .message("Welcome onboarding email sent successfully via SMTP")
                    .build();

        } catch (Exception e) {
            log.error("[NotificationService] Welcome email delivery failed for trackingId={}: {}", trackingId, e.getMessage(), e);
            recordLog(trackingId, request.getTo(), null, subject, "WELCOME", "FAILED", e.getMessage());

            return EmailResponse.builder()
                    .trackingId(trackingId)
                    .recipient(request.getTo())
                    .subject(subject)
                    .status("FAILED")
                    .sentAt(LocalDateTime.now())
                    .message("Failed to send welcome email: " + e.getMessage())
                    .build();
        }
    }

    @Override
    public List<NotificationLog> getNotificationHistory() {
        List<NotificationLog> copy = new ArrayList<>(this.history);
        Collections.reverse(copy);
        return copy;
    }

    private void sendPlain(String to, String cc, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(resolveFromAddress());
        message.setTo(to);
        if (cc != null && !cc.isBlank()) {
            message.setCc(cc);
        }
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    private void sendHtml(String to, String cc, String subject, String htmlContent) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom(resolveFromAddress());
        helper.setTo(to);
        if (cc != null && !cc.isBlank()) {
            helper.setCc(cc);
        }
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);
    }

    private boolean isSimulateMode() {
        if ("true".equalsIgnoreCase(simulateMode)) {
            return true;
        }
        // If mode is auto, simulate when no credentials or mailSender is missing
        if ("auto".equalsIgnoreCase(simulateMode)) {
            return mailSender == null || mailUsername == null || mailUsername.isBlank();
        }
        return false;
    }

    private String resolveFromAddress() {
        return (mailUsername != null && !mailUsername.isBlank()) ? mailUsername : defaultFromAddress;
    }

    private EmailResponse simulateEmailDispatch(String trackingId, String to, String cc, String subject, String body, String type) {
        log.info("""
                
                ========================= [SIMULATED EMAIL DISPATCH] =========================
                Tracking ID : {}
                From        : {}
                To          : {}
                CC          : {}
                Subject     : {}
                Type        : {}
                Body Preview: {}
                =============================================================================
                """, trackingId, resolveFromAddress(), to, cc != null ? cc : "none", subject, type,
                body.length() > 200 ? body.substring(0, 200) + "..." : body);

        recordLog(trackingId, to, cc, subject, type, "SIMULATED", null);

        return EmailResponse.builder()
                .trackingId(trackingId)
                .recipient(to)
                .subject(subject)
                .status("SIMULATED")
                .sentAt(LocalDateTime.now())
                .message("Email simulated in development mode (credentials not configured or simulate=true). See server logs.")
                .build();
    }

    private void recordLog(String trackingId, String to, String cc, String subject, String type, String status, String error) {
        history.add(NotificationLog.builder()
                .trackingId(trackingId)
                .recipient(to)
                .cc(cc)
                .subject(subject)
                .type(type)
                .status(status)
                .errorMessage(error)
                .timestamp(LocalDateTime.now())
                .build());

        // Keep last 100 entries
        if (history.size() > 100) {
            history.remove(0);
        }
    }

    private String buildWelcomeHtmlTemplate(WelcomeEmailRequest request) {
        String dept = request.getDepartment() != null ? request.getDepartment() : "General";
        String designation = request.getDesignation() != null ? request.getDesignation() : "Team Member";
        String tempPass = request.getTemporaryPassword() != null ? request.getTemporaryPassword() : "Available from Admin";

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <style>
                        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #f8fafc; margin: 0; padding: 24px; }
                        .card { background-color: #ffffff; max-width: 600px; margin: 0 auto; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1); border: 1px solid #e2e8f0; }
                        .header { background: linear-gradient(135deg, #4f46e5, #06b6d4); color: #ffffff; padding: 32px 24px; text-align: center; }
                        .header h1 { margin: 0; font-size: 24px; font-weight: 700; }
                        .content { padding: 32px 24px; color: #334155; line-height: 1.6; }
                        .info-box { background-color: #f1f5f9; border-radius: 8px; padding: 16px; margin: 20px 0; border-left: 4px solid #4f46e5; }
                        .info-row { display: flex; justify-content: space-between; margin: 6px 0; font-size: 14px; }
                        .info-label { font-weight: 600; color: #64748b; }
                        .info-value { color: #0f172a; font-weight: 500; }
                        .footer { text-align: center; padding: 20px; font-size: 12px; color: #94a3b8; background-color: #f8fafc; border-top: 1px solid #e2e8f0; }
                    </style>
                </head>
                <body>
                    <div class="card">
                        <div class="header">
                            <h1>Welcome to the Team!</h1>
                        </div>
                        <div class="content">
                            <p>Dear <strong>""" + request.getEmployeeName() + """
                </strong>,</p>
                            <p>We are thrilled to welcome you to our organization! Your employee account has been created on the portal.</p>
                            <div class="info-box">
                                <div class="info-row"><span class="info-label">Department:</span> <span class="info-value">""" + dept + """
                </span></div>
                                <div class="info-row"><span class="info-label">Designation:</span> <span class="info-value">""" + designation + """
                </span></div>
                                <div class="info-row"><span class="info-label">Temporary Password:</span> <span class="info-value"><code>""" + tempPass + """
                </code></span></div>
                            </div>
                            <p>Please log in through the portal to verify your profile and update your password.</p>
                        </div>
                        <div class="footer">
                            <p>&copy; Employee Portal Services. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;
    }
}
