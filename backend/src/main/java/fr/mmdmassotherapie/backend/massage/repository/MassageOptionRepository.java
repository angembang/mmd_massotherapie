package fr.mmdmassotherapie.backend.massage.repository;

import fr.mmdmassotherapie.backend.massage.model.MassageOption;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MassageOptionRepository extends JpaRepository<MassageOption, Long> {
    @EntityGraph(attributePaths = "massage")
    Optional<MassageOption> findByIdAndActiveTrueAndMassageActiveTrue(Long id);
}
