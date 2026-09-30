package fr.mmdmassotherapie.backend.booking.repository;

import fr.mmdmassotherapie.backend.booking.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}
