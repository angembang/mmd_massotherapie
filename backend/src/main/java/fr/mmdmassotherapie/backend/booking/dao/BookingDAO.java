package fr.mmdmassotherapie.backend.booking.dao;

import fr.mmdmassotherapie.backend.booking.model.Booking;
import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import fr.mmdmassotherapie.backend.booking.model.WorkingDay;
import fr.mmdmassotherapie.backend.booking.repository.BookingRepository;
import fr.mmdmassotherapie.backend.booking.repository.BusinessClosureRepository;
import fr.mmdmassotherapie.backend.booking.repository.WorkingDayRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingDAO implements IDAOBooking {
    private static final List<BookingStatus> BLOCKING_STATUSES = List.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED
    );

    private final BookingRepository bookingRepository;
    private final WorkingDayRepository workingDayRepository;
    private final BusinessClosureRepository businessClosureRepository;

    public BookingDAO(
            BookingRepository bookingRepository,
            WorkingDayRepository workingDayRepository,
            BusinessClosureRepository businessClosureRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.workingDayRepository = workingDayRepository;
        this.businessClosureRepository = businessClosureRepository;
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findWithMassageOptionById(id);
    }

    @Override
    public Optional<Booking> findByPublicTokenHash(String publicTokenHash) {
        return bookingRepository.findWithMassageOptionByPublicTokenHash(publicTokenHash);
    }

    @Override
    public List<Booking> findBookings(LocalDate from, LocalDate to, BookingStatus status) {
        if (status == null) {
            return bookingRepository.findByAppointmentDateBetweenOrderByAppointmentDateAscStartTimeAsc(from, to);
        }

        return bookingRepository.findByAppointmentDateBetweenAndStatusOrderByAppointmentDateAscStartTimeAsc(
                from,
                to,
                status
        );
    }

    @Override
    public List<Booking> findBlockingBookings(LocalDate appointmentDate) {
        return bookingRepository.findByAppointmentDateAndStatusInOrderByStartTimeAsc(
                appointmentDate,
                BLOCKING_STATUSES
        );
    }

    @Override
    public Optional<WorkingDay> findActiveWorkingDay(DayOfWeek dayOfWeek) {
        return workingDayRepository.findByDayOfWeekAndActiveTrue(dayOfWeek);
    }

    @Override
    public boolean isClosed(LocalDate date) {
        return businessClosureRepository.existsByClosureDate(date);
    }

    @Override
    public boolean hasBlockingBooking(LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        return !bookingRepository.findBlockingBookingsForUpdate(
                appointmentDate,
                BLOCKING_STATUSES,
                endTime,
                startTime
        ).isEmpty();
    }

    @Override
    public boolean hasBlockingBookingExcluding(
            Long bookingId,
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime
    ) {
        return !bookingRepository.findBlockingBookingsForUpdateExcluding(
                bookingId,
                appointmentDate,
                BLOCKING_STATUSES,
                endTime,
                startTime
        ).isEmpty();
    }
}
