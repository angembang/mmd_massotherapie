package fr.mmdmassotherapie.backend.massage.repository;

import fr.mmdmassotherapie.backend.massage.model.Massage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MassageRepository extends JpaRepository<Massage, Long> {
    @EntityGraph(attributePaths = "options")
    List<Massage> findByActiveTrueOrderByDisplayOrderAsc();

    @EntityGraph(attributePaths = "options")
    Optional<Massage> findBySlugAndActiveTrue(String slug);
}
