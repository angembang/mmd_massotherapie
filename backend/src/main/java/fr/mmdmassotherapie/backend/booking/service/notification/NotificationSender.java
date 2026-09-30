package fr.mmdmassotherapie.backend.booking.service.notification;

public interface NotificationSender {
    NotificationSendResult send(NotificationMessage message);
}
