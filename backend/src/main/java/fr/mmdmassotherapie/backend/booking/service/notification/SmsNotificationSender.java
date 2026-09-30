package fr.mmdmassotherapie.backend.booking.service.notification;

import fr.mmdmassotherapie.backend.booking.model.NotificationChannel;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationSender implements NotificationSender {
    @Override
    public NotificationSendResult send(NotificationMessage message) {
        if (message.channel() != NotificationChannel.SMS) {
            return NotificationSendResult.failed("Unsupported notification channel for SMS sender");
        }

        return NotificationSendResult.failed("SMS notifications are not implemented yet");
    }
}
