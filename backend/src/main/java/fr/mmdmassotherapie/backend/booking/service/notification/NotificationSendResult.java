package fr.mmdmassotherapie.backend.booking.service.notification;

public record NotificationSendResult(
        boolean sent,
        String providerMessageId,
        String errorMessage
) {
    public static NotificationSendResult sent(String providerMessageId) {
        return new NotificationSendResult(true, providerMessageId, null);
    }

    public static NotificationSendResult failed(String errorMessage) {
        return new NotificationSendResult(false, null, errorMessage);
    }
}
