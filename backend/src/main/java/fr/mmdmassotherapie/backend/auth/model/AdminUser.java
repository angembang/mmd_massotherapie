package fr.mmdmassotherapie.backend.auth.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;

@Getter
@Entity
@Table(name = "admin_user")
public class AdminUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AdminRole role = AdminRole.ROLE_ADMIN;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    protected AdminUser() {
    }

    public AdminUser(String email, String passwordHash, String fullName) {
        this.email = normalizeEmail(email);
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
