package fr.mmdmassotherapie.backend.auth.model;

public record AdminUserResponse(
        Long id,
        String email,
        String fullName,
        AdminRole role
) {
}
