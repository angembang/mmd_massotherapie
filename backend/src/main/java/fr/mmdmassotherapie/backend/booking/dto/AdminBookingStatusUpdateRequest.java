package fr.mmdmassotherapie.backend.booking.dto;

import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AdminBookingStatusUpdateRequest(
        @NotNull
        BookingStatus status,

        @Size(max = 2000)
        String internalNote
) {
}
