package fr.mmdmassotherapie.backend.booking.dto;

import java.time.LocalTime;

public record AvailableSlotResponse(
        LocalTime startTime,
        LocalTime endTime
) {
}
