ALTER TABLE booking
    ADD public_token_hash VARCHAR(128) NULL,
    ADD public_token_expires_at TIMESTAMP NULL,
    ADD cancellation_reason TEXT NULL,
    ADD cancelled_by VARCHAR(30) NULL,
    ADD cancelled_at TIMESTAMP NULL;

ALTER TABLE booking
    ADD CONSTRAINT uk_booking_public_token_hash UNIQUE (public_token_hash);

ALTER TABLE booking
    ADD CONSTRAINT chk_booking_cancelled_by
        CHECK (cancelled_by IS NULL OR cancelled_by IN ('CUSTOMER', 'ADMIN'));

ALTER TABLE notification_log
    ADD message_body TEXT NULL;
