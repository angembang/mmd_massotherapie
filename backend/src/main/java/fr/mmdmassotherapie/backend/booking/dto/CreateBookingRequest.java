package fr.mmdmassotherapie.backend.booking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateBookingRequest(
        @NotBlank
        @Size(max = 150)
        String customerName,

        @Email
        @Size(max = 150)
        String customerEmail,

        @Size(max = 30)
        String customerPhone,

        @NotNull
        Long massageOptionId,

        @NotNull
        @FutureOrPresent
        LocalDate appointmentDate,

        @NotNull
        LocalTime startTime,

        @Size(max = 2000)
        String customerMessage
) {
}
