package fr.mmdmassotherapie.backend.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(min = 12, max = 100)
        String password,

        @NotBlank
        @Size(max = 150)
        String fullName,

        @NotBlank
        @Size(max = 255)
        String invitationCode
) {
}
