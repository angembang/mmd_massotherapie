package fr.mmdmassotherapie.backend.booking.repository;

import fr.mmdmassotherapie.backend.booking.model.BusinessClosure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BusinessClosureRepository extends JpaRepository<BusinessClosure, Long> {
    boolean existsByClosureDate(LocalDate closureDate);
}
