package fr.mmdmassotherapie.backend.massage;

import fr.mmdmassotherapie.backend.massage.dao.IDAOMassage;
import fr.mmdmassotherapie.backend.massage.model.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MassageService {
    private final IDAOMassage massageIDAO;

    public MassageService(IDAOMassage massageIDAO) {
        this.massageIDAO = massageIDAO;
    }

    public List<MassageResponse> getActiveMassages() {
        return massageIDAO.findActiveMassages()
                .stream()
                .map(this::toMassageResponse)
                .toList();
    }

    public MassageDetailResponse getMassageBySlug(String slug) {
        Massage massage = massageIDAO.findActiveBySlug(slug)
                .orElseThrow(() -> new MassageNotFoundException(slug));

        return toMassageDetailResponse(massage);
    }

    private MassageResponse toMassageResponse(Massage massage) {
        return new MassageResponse(
                massage.getId(),
                massage.getName(),
                massage.getSlug(),
                massage.getIcon(),
                massage.getImage(),
                activeOptions(massage)
        );
    }

    private MassageDetailResponse toMassageDetailResponse(Massage massage) {
        return new MassageDetailResponse(
                massage.getId(),
                massage.getName(),
                massage.getSlug(),
                massage.getDescription(),
                massage.getIcon(),
                massage.getImage(),
                activeOptions(massage)
        );
    }

    private List<MassageOptionResponse> activeOptions(Massage massage) {
        return massage.getOptions()
                .stream()
                .filter(MassageOption::isActive)
                .sorted(Comparator.comparingInt(MassageOption::getDisplayOrder))
                .map(option -> new MassageOptionResponse(
                        option.getId(),
                        option.getDurationMinutes(),
                        option.getBodyArea(),
                        option.getPriceCents()
                ))
                .toList();
    }
}
