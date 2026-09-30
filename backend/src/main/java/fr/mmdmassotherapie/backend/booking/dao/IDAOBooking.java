package fr.mmdmassotherapie.backend.booking.dao;

import fr.mmdmassotherapie.backend.booking.model.Booking;
import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import fr.mmdmassotherapie.backend.booking.model.WorkingDay;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IDAOBooking {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    Optional<Booking> findByPublicTokenHash(String publicTokenHash);

    List<Booking> findBookings(LocalDate from, LocalDate to, BookingStatus status);

    List<Booking> findBlockingBookings(LocalDate appointmentDate);

    Optional<WorkingDay> findActiveWorkingDay(DayOfWeek dayOfWeek);

    boolean isClosed(LocalDate date);

    boolean hasBlockingBooking(LocalDate appointmentDate, LocalTime startTime, LocalTime endTime);

    boolean hasBlockingBookingExcluding(Long bookingId, LocalDate appointmentDate, LocalTime startTime, LocalTime endTime);
}
