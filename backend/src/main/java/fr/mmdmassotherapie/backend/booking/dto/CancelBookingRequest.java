package fr.mmdmassotherapie.backend.booking.dto;

import jakarta.validation.constraints.Size;

public record CancelBookingRequest(
        @Size(max = 2000)
        String reason
) {
}
