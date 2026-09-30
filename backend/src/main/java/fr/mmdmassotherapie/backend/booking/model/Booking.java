package fr.mmdmassotherapie.backend.booking.model;

import fr.mmdmassotherapie.backend.massage.model.BodyArea;
import fr.mmdmassotherapie.backend.massage.model.MassageOption;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "customer_email", length = 150)
    private String customerEmail;

    @Column(name = "customer_phone", length = 30)
    private String customerPhone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "massage_option_id", nullable = false)
    private MassageOption massageOption;

    @Column(name = "massage_name_snapshot", nullable = false, length = 150)
    private String massageNameSnapshot;

    @Column(name = "duration_minutes_snapshot", nullable = false)
    private int durationMinutesSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_area_snapshot", nullable = false, length = 50)
    private BodyArea bodyAreaSnapshot;

    @Column(name = "price_cents_snapshot", nullable = false)
    private int priceCentsSnapshot;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BookingStatus status = BookingStatus.CONFIRMED;

    @Column(name = "customer_message", columnDefinition = "TEXT")
    private String customerMessage;

    @Column(name = "internal_note", columnDefinition = "TEXT")
    private String internalNote;

    @Column(name = "public_token_hash", unique = true, length = 128)
    private String publicTokenHash;

    @Column(name = "public_token_expires_at")
    private Instant publicTokenExpiresAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancelled_by", length = 30)
    private BookingCancellationActor cancelledBy;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    protected Booking() {
    }

    public Booking(
            String customerName,
            String customerEmail,
            String customerPhone,
            MassageOption massageOption,
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime,
            String customerMessage
    ) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.massageOption = massageOption;
        this.massageNameSnapshot = massageOption.getMassage().getName();
        this.durationMinutesSnapshot = massageOption.getDurationMinutes();
        this.bodyAreaSnapshot = massageOption.getBodyArea();
        this.priceCentsSnapshot = massageOption.getPriceCents();
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerMessage = customerMessage;
    }

    public void updateStatus(BookingStatus status, String internalNote) {
        this.status = status;
        this.internalNote = internalNote;
    }

    public void updateInternalNote(String internalNote) {
        this.internalNote = internalNote;
    }

    public void setPublicManagementToken(String publicTokenHash, Instant publicTokenExpiresAt) {
        this.publicTokenHash = publicTokenHash;
        this.publicTokenExpiresAt = publicTokenExpiresAt;
    }

    public void reschedule(LocalDate appointmentDate, LocalTime startTime, LocalTime endTime) {
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void cancel(BookingCancellationActor cancelledBy, String cancellationReason) {
        this.status = BookingStatus.CANCELLED;
        this.cancelledBy = cancelledBy;
        this.cancellationReason = cancellationReason;
        this.cancelledAt = Instant.now();
    }
}
