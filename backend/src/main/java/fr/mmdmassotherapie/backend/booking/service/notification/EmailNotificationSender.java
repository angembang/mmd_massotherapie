package fr.mmdmassotherapie.backend.booking.service.notification;

import fr.mmdmassotherapie.backend.booking.model.NotificationChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationSender implements NotificationSender {
    private final JavaMailSender mailSender;
    private final boolean enabled;
    private final String from;
    private final String replyTo;

    public EmailNotificationSender(
            JavaMailSender mailSender,
            @Value("${app.notifications.email.enabled}") boolean enabled,
            @Value("${app.notifications.email.from}") String from,
            @Value("${app.notifications.email.reply-to}") String replyTo
    ) {
        this.mailSender = mailSender;
        this.enabled = enabled;
        this.from = from;
        this.replyTo = replyTo;
    }

    @Override
    public NotificationSendResult send(NotificationMessage message) {
        if (message.channel() != NotificationChannel.EMAIL) {
            return NotificationSendResult.failed("Unsupported notification channel for email sender");
        }

        if (!enabled) {
            return NotificationSendResult.failed("Email notifications are disabled");
        }

        if (from == null || from.isBlank()) {
            return NotificationSendResult.failed("Email sender address is not configured");
        }

        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(from);
            mailMessage.setTo(message.recipient());
            mailMessage.setSubject(message.subject());
            mailMessage.setText(message.body());

            if (replyTo != null && !replyTo.isBlank()) {
                mailMessage.setReplyTo(replyTo);
            }

            mailSender.send(mailMessage);
            return NotificationSendResult.sent(null);
        } catch (MailException exception) {
            return NotificationSendResult.failed(safeErrorMessage(exception));
        }
    }

    private String safeErrorMessage(MailException exception) {
        String message = exception.getMessage();

        if (message == null || message.isBlank()) {
            return "Email provider rejected the message";
        }

        return message.length() > 1000 ? message.substring(0, 1000) : message;
    }
}
