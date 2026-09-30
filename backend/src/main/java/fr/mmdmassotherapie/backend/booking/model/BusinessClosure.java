package fr.mmdmassotherapie.backend.booking.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "business_closure")
public class BusinessClosure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "closure_date", nullable = false, unique = true)
    private LocalDate closureDate;

    @Column(length = 255)
    private String reason;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    protected BusinessClosure() {
    }
}
