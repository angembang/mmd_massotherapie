package fr.mmdmassotherapie.backend.booking.service.notification;

import fr.mmdmassotherapie.backend.booking.model.NotificationChannel;

public record NotificationMessage(
        NotificationChannel channel,
        String recipient,
        String subject,
        String body
) {
}
