package fr.mmdmassotherapie.backend.auth;

import fr.mmdmassotherapie.backend.auth.dao.IDAOAuth;
import fr.mmdmassotherapie.backend.auth.exception.AuthConflictException;
import fr.mmdmassotherapie.backend.auth.exception.AuthUnauthorizedException;
import fr.mmdmassotherapie.backend.auth.exception.AuthValidationException;
import fr.mmdmassotherapie.backend.auth.model.*;
import fr.mmdmassotherapie.backend.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AuthService {
    private static final Pattern UPPERCASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWERCASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("\\d");
    private static final Pattern SPECIAL = Pattern.compile("[^a-zA-Z0-9]");

    private final IDAOAuth authDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.security.admin.registration-invitation-code}")
    private String registrationInvitationCode;

    public AuthService(IDAOAuth authDAO, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authDAO = authDAO;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateInvitationCode(request.invitationCode());
        validatePasswordStrength(request.password());

        String email = AdminUser.normalizeEmail(request.email());

        if (authDAO.existsByEmail(email)) {
            throw new AuthConflictException("Admin user already exists");
        }

        AdminUser adminUser = new AdminUser(
                email,
                passwordEncoder.encode(request.password()),
                requiredTrim(request.fullName(), "Full name is required")
        );

        AdminUser savedUser = authDAO.save(adminUser);

        return createAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = AdminUser.normalizeEmail(request.email());

        AdminUser adminUser = authDAO.findByEmail(email)
                .filter(AdminUser::isActive)
                .orElseThrow(AuthUnauthorizedException::new);

        if (!passwordEncoder.matches(request.password(), adminUser.getPasswordHash())) {
            throw new AuthUnauthorizedException();
        }

        return createAuthResponse(adminUser);
    }

    private AuthResponse createAuthResponse(AdminUser adminUser) {
        String token = jwtService.generateToken(
                adminUser.getEmail(),
                List.of(adminUser.getRole().name())
        );

        return new AuthResponse(
                "Bearer",
                token,
                jwtService.getExpirationMs(),
                toResponse(adminUser)
        );
    }

    private AdminUserResponse toResponse(AdminUser adminUser) {
        return new AdminUserResponse(
                adminUser.getId(),
                adminUser.getEmail(),
                adminUser.getFullName(),
                adminUser.getRole()
        );
    }

    private void validateInvitationCode(String invitationCode) {
        if (registrationInvitationCode == null || registrationInvitationCode.isBlank()) {
            throw new IllegalStateException("Admin registration invitation code is not configured");
        }

        byte[] expected = registrationInvitationCode.getBytes(StandardCharsets.UTF_8);
        byte[] actual = invitationCode.getBytes(StandardCharsets.UTF_8);

        if (!MessageDigest.isEqual(expected, actual)) {
            throw new AuthUnauthorizedException();
        }
    }

    private void validatePasswordStrength(String password) {
        if (!UPPERCASE.matcher(password).find()
                || !LOWERCASE.matcher(password).find()
                || !DIGIT.matcher(password).find()
                || !SPECIAL.matcher(password).find()) {
            throw new AuthValidationException(
                    "Password must contain uppercase, lowercase, digit and special character"
            );
        }
    }

    private String requiredTrim(String value, String message) {
        String trimmedValue = value == null ? null : value.trim();

        if (trimmedValue == null || trimmedValue.isBlank()) {
            throw new AuthValidationException(message);
        }

        return trimmedValue;
    }
}
