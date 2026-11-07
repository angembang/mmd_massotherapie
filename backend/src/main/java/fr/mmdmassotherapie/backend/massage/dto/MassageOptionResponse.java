package fr.mmdmassotherapie.backend.massage.dto;

import fr.mmdmassotherapie.backend.massage.model.BodyArea;

public record MassageOptionResponse(Long id,
                                    int durationMinutes,
                                    BodyArea bodyArea,
                                    int priceCents) {}
