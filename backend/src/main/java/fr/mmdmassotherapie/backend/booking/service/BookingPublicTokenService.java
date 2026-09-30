package fr.mmdmassotherapie.backend.booking.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class BookingPublicTokenService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int TOKEN_BYTES = 32;

    @Value("${app.booking.public-token.secret}")
    private String publicTokenSecret;

    @Value("${app.booking.public-token.expiration-days}")
    private long expirationDays;

    @Value("${app.booking.management-base-url}")
    private String managementBaseUrl;

    private final SecureRandom secureRandom = new SecureRandom();
    private SecretKey secretKey;

    @PostConstruct
    void init() {
        byte[] keyBytes = Base64.getDecoder().decode(publicTokenSecret);

        if (keyBytes.length < 32) {
            throw new IllegalStateException("Booking public token secret must be at least 256 bits after Base64 decoding");
        }

        if (expirationDays <= 0) {
            throw new IllegalStateException("Booking public token expiration must be greater than 0");
        }

        this.secretKey = new SecretKeySpec(keyBytes, HMAC_ALGORITHM);
    }

    public String generateToken() {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }

    public String hashToken(String token) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKey);
            return HexFormat.of().formatHex(mac.doFinal(token.getBytes()));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash booking public token", exception);
        }
    }

    public Instant expiresAt() {
        return Instant.now().plus(Duration.ofDays(expirationDays));
    }

    public String managementUrl(String token) {
        String normalizedBaseUrl = managementBaseUrl.endsWith("/")
                ? managementBaseUrl.substring(0, managementBaseUrl.length() - 1)
                : managementBaseUrl;

        return normalizedBaseUrl + "/bookings/manage/" + token;
    }
}
