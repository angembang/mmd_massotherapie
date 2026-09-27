package fr.mmdmassotherapie.backend.massage;

import fr.mmdmassotherapie.backend.massage.model.MassageDetailResponse;
import fr.mmdmassotherapie.backend.massage.model.MassageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/massages")
public class MassageController {

    private final MassageService massageService;

    public MassageController(MassageService massageService) {
        this.massageService = massageService;
    }

    @GetMapping
    public List<MassageResponse> getMassages() {
        return massageService.getActiveMassages();
    }

    @GetMapping("/{slug}")
    public MassageDetailResponse getMassageBySlug(@PathVariable String slug) {
        return massageService.getMassageBySlug(slug);
    }
}
