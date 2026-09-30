package fr.mmdmassotherapie.backend.booking.dto;

import fr.mmdmassotherapie.backend.booking.model.BookingCancellationActor;
import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import fr.mmdmassotherapie.backend.massage.model.BodyArea;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public record BookingResponse(
        Long id,
        String customerName,
        String customerEmail,
        String customerPhone,
        Long massageOptionId,
        String massageName,
        int durationMinutes,
        BodyArea bodyArea,
        int priceCents,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        BookingStatus status,
        String customerMessage,
        String internalNote,
        String cancellationReason,
        BookingCancellationActor cancelledBy,
        Instant cancelledAt,
        Instant createdAt,
        Instant updatedAt
) {
}
