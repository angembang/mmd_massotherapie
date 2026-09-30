package fr.mmdmassotherapie.backend.booking.service;

import fr.mmdmassotherapie.backend.booking.model.Booking;
import fr.mmdmassotherapie.backend.booking.model.NotificationChannel;
import fr.mmdmassotherapie.backend.booking.model.NotificationLog;
import fr.mmdmassotherapie.backend.booking.model.NotificationStatus;
import fr.mmdmassotherapie.backend.booking.repository.NotificationLogRepository;
import fr.mmdmassotherapie.backend.booking.service.notification.EmailNotificationSender;
import fr.mmdmassotherapie.backend.booking.service.notification.NotificationMessage;
import fr.mmdmassotherapie.backend.booking.service.notification.NotificationSendResult;
import fr.mmdmassotherapie.backend.booking.service.notification.NotificationSender;
import fr.mmdmassotherapie.backend.booking.service.notification.SmsNotificationSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class BookingNotificationService {
    private final NotificationLogRepository notificationLogRepository;
    private final NotificationSender emailNotificationSender;
    private final NotificationSender smsNotificationSender;
    private final TransactionTemplate transactionTemplate;

    public BookingNotificationService(
            NotificationLogRepository notificationLogRepository,
            EmailNotificationSender emailNotificationSender,
            SmsNotificationSender smsNotificationSender,
            PlatformTransactionManager transactionManager
    ) {
        this.notificationLogRepository = notificationLogRepository;
        this.emailNotificationSender = emailNotificationSender;
        this.smsNotificationSender = smsNotificationSender;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Transactional
    public void sendBookingConfirmation(Booking booking, String managementUrl) {
        String body = """
                Bonjour %s,

                Votre rendez-vous est confirmé.

                Prestation : %s
                Date : %s
                Horaire : %s - %s

                Vous pouvez le consulter, le modifier ou l'annuler ici :
                %s
                """.formatted(
                booking.getCustomerName(),
                booking.getMassageNameSnapshot(),
                booking.getAppointmentDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                managementUrl
        );

        registerNotification(
                booking,
                "Confirmation de votre rendez-vous",
                body,
                "Booking confirmation notification"
        );
    }

    @Transactional
    public void sendCustomerCancellationConfirmation(Booking booking) {
        String body = """
                Bonjour %s,

                Votre rendez-vous du %s a %s a bien été annulé.
                """.formatted(
                booking.getCustomerName(),
                booking.getAppointmentDate(),
                booking.getStartTime()
        );

        registerNotification(
                booking,
                "Annulation de votre rendez-vous",
                body,
                "Customer cancellation confirmation notification"
        );
    }

    @Transactional
    public void sendAdminCancellationNotice(Booking booking, String reason) {
        String body = """
                Bonjour %s,

                Votre rendez-vous du %s a %s a été annulé.
                %s

                Merci de choisir un autre créneau si possible.
                """.formatted(
                booking.getCustomerName(),
                booking.getAppointmentDate(),
                booking.getStartTime(),
                reason == null ? "" : "Motif : " + reason
        );

        registerNotification(
                booking,
                "Annulation de votre rendez-vous",
                body,
                "Admin cancellation notice notification"
        );
    }

    @Transactional
    public void sendBookingRescheduled(Booking booking, String managementUrl) {
        String body = """
                Bonjour %s,

                Votre rendez-vous a été modifié.

                Prestation : %s
                Nouvelle date : %s
                Nouvel horaire : %s - %s

                Vous pouvez le consulter ici :
                %s
                """.formatted(
                booking.getCustomerName(),
                booking.getMassageNameSnapshot(),
                booking.getAppointmentDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                managementUrl
        );

        registerNotification(
                booking,
                "Modification de votre rendez-vous",
                body,
                "Booking rescheduled notification"
        );
    }

    private void registerNotification(Booking booking, String subject, String body, String logMessageBody) {
        if (booking.getCustomerEmail() != null) {
            NotificationLog notificationLog = notificationLogRepository.saveAndFlush(new NotificationLog(
                    booking,
                    NotificationChannel.EMAIL,
                    booking.getCustomerEmail(),
                    NotificationStatus.PENDING,
                    null,
                    logMessageBody,
                    null,
                    null
            ));

            sendAfterCommit(notificationLog.getId(), new NotificationMessage(
                    NotificationChannel.EMAIL,
                    booking.getCustomerEmail(),
                    subject,
                    body
            ));
            return;
        }

        NotificationLog notificationLog = notificationLogRepository.saveAndFlush(new NotificationLog(
                booking,
                NotificationChannel.SMS,
                booking.getCustomerPhone(),
                NotificationStatus.PENDING,
                null,
                logMessageBody,
                null,
                null
        ));

        sendAfterCommit(notificationLog.getId(), new NotificationMessage(
                NotificationChannel.SMS,
                booking.getCustomerPhone(),
                subject,
                body
        ));
    }

    private void sendAfterCommit(Long notificationLogId, NotificationMessage message) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    sendAndUpdateLog(notificationLogId, message);
                }
            });
            return;
        }

        sendAndUpdateLog(notificationLogId, message);
    }

    private void sendAndUpdateLog(Long notificationLogId, NotificationMessage message) {
        NotificationSendResult result = senderFor(message.channel()).send(message);

        transactionTemplate.executeWithoutResult(status -> notificationLogRepository
                .findById(notificationLogId)
                .ifPresent(notificationLog -> {
                    if (result.sent()) {
                        notificationLog.markSent(result.providerMessageId());
                    } else {
                        notificationLog.markFailed(result.errorMessage());
                    }
                    notificationLogRepository.save(notificationLog);
                }));
    }

    private NotificationSender senderFor(NotificationChannel channel) {
        return switch (channel) {
            case EMAIL -> emailNotificationSender;
            case SMS -> smsNotificationSender;
        };
    }
}
