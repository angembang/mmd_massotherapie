package fr.mmdmassotherapie.backend.booking.repository;

import fr.mmdmassotherapie.backend.booking.model.Booking;
import fr.mmdmassotherapie.backend.booking.model.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select booking
            from Booking booking
            where booking.appointmentDate = :appointmentDate
              and booking.status in :statuses
              and booking.startTime < :requestedEndTime
              and booking.endTime > :requestedStartTime
            """)
    List<Booking> findBlockingBookingsForUpdate(
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") Collection<BookingStatus> statuses,
            @Param("requestedEndTime") LocalTime requestedEndTime,
            @Param("requestedStartTime") LocalTime requestedStartTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select booking
            from Booking booking
            where booking.id <> :excludedBookingId
              and booking.appointmentDate = :appointmentDate
              and booking.status in :statuses
              and booking.startTime < :requestedEndTime
              and booking.endTime > :requestedStartTime
            """)
    List<Booking> findBlockingBookingsForUpdateExcluding(
            @Param("excludedBookingId") Long excludedBookingId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("statuses") Collection<BookingStatus> statuses,
            @Param("requestedEndTime") LocalTime requestedEndTime,
            @Param("requestedStartTime") LocalTime requestedStartTime
    );

    @EntityGraph(attributePaths = "massageOption")
    Optional<Booking> findWithMassageOptionById(Long id);

    @EntityGraph(attributePaths = "massageOption")
    Optional<Booking> findWithMassageOptionByPublicTokenHash(String publicTokenHash);

    @EntityGraph(attributePaths = "massageOption")
    List<Booking> findByAppointmentDateBetweenOrderByAppointmentDateAscStartTimeAsc(
            LocalDate from,
            LocalDate to
    );

    @EntityGraph(attributePaths = "massageOption")
    List<Booking> findByAppointmentDateBetweenAndStatusOrderByAppointmentDateAscStartTimeAsc(
            LocalDate from,
            LocalDate to,
            BookingStatus status
    );

    List<Booking> findByAppointmentDateAndStatusInOrderByStartTimeAsc(
            LocalDate appointmentDate,
            Collection<BookingStatus> statuses
    );
}
