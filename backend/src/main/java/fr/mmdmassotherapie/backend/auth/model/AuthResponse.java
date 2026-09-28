package fr.mmdmassotherapie.backend.auth.model;

public record AuthResponse(
        String tokenType,
        String accessToken,
        long expiresInMs,
        AdminUserResponse user
) {
}
