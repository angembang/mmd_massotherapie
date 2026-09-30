package fr.mmdmassotherapie.backend.booking.controller;

import fr.mmdmassotherapie.backend.bo.LogicResult;
import fr.mmdmassotherapie.backend.booking.service.BookingService;
import fr.mmdmassotherapie.backend.booking.dto.AdminBookingNoteUpdateRequest;
import fr.mmdmassotherapie.backend.booking.dto.AdminBookingStatusUpdateRequest;
import fr.mmdmassotherapie.backend.booking.dto.BookingResponse;
import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import fr.mmdmassotherapie.backend.booking.dto.CancelBookingRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {
    private final BookingService bookingService;

    public AdminBookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> getBookings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) BookingStatus status
    ) {
        return bookingService.getAdminBookings(from, to, status);
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable Long id) {
        return bookingService.getAdminBooking(id);
    }

    @PatchMapping("/{id}/status")
    public LogicResult<BookingResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AdminBookingStatusUpdateRequest request
    ) {
        return new LogicResult<>(
                "BOOKING_STATUS_UPDATED",
                "Booking status updated",
                bookingService.updateStatus(id, request)
        );
    }

    @PatchMapping("/{id}/note")
    public LogicResult<BookingResponse> updateInternalNote(
            @PathVariable Long id,
            @Valid @RequestBody AdminBookingNoteUpdateRequest request
    ) {
        return new LogicResult<>(
                "BOOKING_NOTE_UPDATED",
                "Booking internal note updated",
                bookingService.updateInternalNote(id, request)
        );
    }

    @PatchMapping("/{id}/cancel")
    public LogicResult<BookingResponse> cancelBooking(
            @PathVariable Long id,
            @Valid @RequestBody CancelBookingRequest request
    ) {
        return new LogicResult<>(
                "BOOKING_CANCELLED",
                "Booking cancelled",
                bookingService.cancelAdminBooking(id, request)
        );
    }
}
