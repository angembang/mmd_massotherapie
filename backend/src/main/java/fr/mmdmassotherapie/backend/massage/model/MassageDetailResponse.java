package fr.mmdmassotherapie.backend.massage.model;

import java.util.List;

public record MassageDetailResponse(Long id,
                                    String name,
                                    String slug,
                                    String description,
                                    String icon,
                                    String image,
                                    List<MassageOptionResponse> options) {}
