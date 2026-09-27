package fr.mmdmassotherapie.backend.massage.model;

public record MassageOptionResponse(Long id,
                                    int durationMinutes,
                                    BodyArea bodyArea,
                                    int priceCents) {}
