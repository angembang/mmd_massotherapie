package fr.mmdmassotherapie.backend.massage.dao;

import fr.mmdmassotherapie.backend.massage.model.Massage;
import fr.mmdmassotherapie.backend.massage.model.MassageOption;

import java.util.List;
import java.util.Optional;

public interface IDAOMassage {

    List<Massage> findActiveMassages();

    Optional<Massage> findActiveBySlug(String slug);

    Optional<MassageOption> findActiveOptionById(Long optionId);
}
