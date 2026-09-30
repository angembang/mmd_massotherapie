package fr.mmdmassotherapie.backend.booking.dto;

public record BookingCreatedResponse(
        PublicBookingResponse booking,
        String managementUrl
) {
}
