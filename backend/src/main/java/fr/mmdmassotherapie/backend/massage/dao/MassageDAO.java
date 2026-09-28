package fr.mmdmassotherapie.backend.massage.dao;

import fr.mmdmassotherapie.backend.massage.repository.MassageOptionRepository;
import fr.mmdmassotherapie.backend.massage.repository.MassageRepository;
import fr.mmdmassotherapie.backend.massage.model.Massage;
import fr.mmdmassotherapie.backend.massage.model.MassageOption;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MassageDAO implements IDAOMassage {

    private final MassageRepository massageRepository;
    private final MassageOptionRepository massageOptionRepository;

    public MassageDAO(
            MassageRepository massageRepository,
            MassageOptionRepository massageOptionRepository
    ) {
        this.massageRepository = massageRepository;
        this.massageOptionRepository = massageOptionRepository;
    }

    @Override
    public List<Massage> findActiveMassages() {

        return massageRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }

    @Override
    public Optional<Massage> findActiveBySlug(String slug) {

        return massageRepository.findBySlugAndActiveTrue(slug);
    }

    @Override
    public Optional<MassageOption> findActiveOptionById(Long optionId) {
        return massageOptionRepository.findByIdAndActiveTrueAndMassageActiveTrue(optionId);
    }
}
