package fr.mmdmassotherapie.backend.booking.dto;

import jakarta.validation.constraints.Size;

public record AdminBookingNoteUpdateRequest(
        @Size(max = 2000)
        String internalNote
) {
}
