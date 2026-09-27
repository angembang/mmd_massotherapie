package fr.mmdmassotherapie.backend.massage.model;

import java.util.List;

public record MassageResponse(Long id,
                              String name,
                              String slug,
                              String icon,
                              String image,
                              List<MassageOptionResponse> options) {}
