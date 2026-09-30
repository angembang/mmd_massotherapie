package fr.mmdmassotherapie.backend.booking.repository;

import fr.mmdmassotherapie.backend.booking.model.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    Optional<WorkingDay> findByDayOfWeekAndActiveTrue(DayOfWeek dayOfWeek);
}
