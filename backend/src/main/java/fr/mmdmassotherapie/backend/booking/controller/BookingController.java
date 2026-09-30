package fr.mmdmassotherapie.backend.booking.controller;

import fr.mmdmassotherapie.backend.bo.LogicResult;
import fr.mmdmassotherapie.backend.booking.service.BookingService;
import fr.mmdmassotherapie.backend.booking.dto.AvailableSlotResponse;
import fr.mmdmassotherapie.backend.booking.dto.BookingCreatedResponse;
import fr.mmdmassotherapie.backend.booking.dto.CancelBookingRequest;
import fr.mmdmassotherapie.backend.booking.dto.CreateBookingRequest;
import fr.mmdmassotherapie.backend.booking.dto.CustomerBookingRescheduleRequest;
import fr.mmdmassotherapie.backend.booking.dto.PublicBookingResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/available-slots")
    public List<AvailableSlotResponse> getAvailableSlots(
            @RequestParam Long massageOptionId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return bookingService.getAvailableSlots(massageOptionId, date);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogicResult<BookingCreatedResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return new LogicResult<>(
                "BOOKING_CREATED",
                "Booking confirmed",
                bookingService.createBooking(request)
        );
    }

    @GetMapping("/manage/{token}")
    public PublicBookingResponse getManagedBooking(@PathVariable String token) {
        return bookingService.getPublicBooking(token);
    }

    @PatchMapping("/manage/{token}/reschedule")
    public LogicResult<BookingCreatedResponse> rescheduleManagedBooking(
            @PathVariable String token,
            @Valid @RequestBody CustomerBookingRescheduleRequest request
    ) {
        return new LogicResult<>(
                "BOOKING_RESCHEDULED",
                "Booking rescheduled",
                bookingService.reschedulePublicBooking(token, request)
        );
    }

    @PatchMapping("/manage/{token}/cancel")
    public LogicResult<PublicBookingResponse> cancelManagedBooking(
            @PathVariable String token,
            @Valid @RequestBody CancelBookingRequest request
    ) {
        return new LogicResult<>(
                "BOOKING_CANCELLED",
                "Booking cancelled",
                bookingService.cancelPublicBooking(token, request)
        );
    }
}
