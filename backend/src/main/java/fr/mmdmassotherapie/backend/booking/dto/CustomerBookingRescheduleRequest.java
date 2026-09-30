package fr.mmdmassotherapie.backend.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record CustomerBookingRescheduleRequest(
        @NotNull
        @FutureOrPresent
        LocalDate appointmentDate,

        @NotNull
        LocalTime startTime
) {
}
