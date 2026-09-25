CREATE TABLE massage (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    slug VARCHAR(180) NOT NULL UNIQUE,
    description TEXT,
    icon VARCHAR(255),
    image VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE massage_option (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    massage_id BIGINT NOT NULL,
    duration_minutes INT NOT NULL,
    body_area VARCHAR(50) NOT NULL,
    price_cents INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_massage_option_massage
        FOREIGN KEY (massage_id)
            REFERENCES massage(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_massage_option_duration
        CHECK (duration_minutes > 0),

    CONSTRAINT chk_massage_option_price
        CHECK (price_cents >= 0),

    CONSTRAINT chk_massage_option_body_area
        CHECK (body_area IN ('UPPER_OR_LOWER_BODY', 'FULL_BODY', 'NOT_APPLICABLE'))
);

CREATE TABLE working_day (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    day_of_week VARCHAR(20) NOT NULL,
    opening_time TIME NOT NULL,
    closing_time TIME NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_working_day_day_of_week
        UNIQUE (day_of_week),

    CONSTRAINT chk_working_day_day_of_week
        CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),

    CONSTRAINT chk_working_day_hours
        CHECK (opening_time < closing_time)
);

CREATE TABLE business_closure (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    closure_date DATE NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_business_closure_date
        UNIQUE (closure_date)
);

CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(150) NOT NULL,
    customer_email VARCHAR(150),
    customer_phone VARCHAR(30),
    massage_option_id BIGINT NOT NULL,

    massage_name_snapshot VARCHAR(150) NOT NULL,
    duration_minutes_snapshot INT NOT NULL,
    body_area_snapshot VARCHAR(50) NOT NULL,
    price_cents_snapshot INT NOT NULL,

    appointment_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    customer_message TEXT,
    internal_note TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_massage_option
        FOREIGN KEY (massage_option_id)
            REFERENCES massage_option(id)
            ON DELETE RESTRICT,

    CONSTRAINT chk_booking_contact_required
        CHECK (customer_email IS NOT NULL OR customer_phone IS NOT NULL),

    CONSTRAINT chk_booking_time
        CHECK (start_time < end_time),

    CONSTRAINT chk_booking_status
        CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED')),

    CONSTRAINT chk_booking_duration_snapshot
        CHECK (duration_minutes_snapshot > 0),

    CONSTRAINT chk_booking_price_snapshot
        CHECK (price_cents_snapshot >= 0),

    CONSTRAINT chk_booking_body_area_snapshot
        CHECK (body_area_snapshot IN ('UPPER_OR_LOWER_BODY', 'FULL_BODY', 'NOT_APPLICABLE'))
);

CREATE INDEX idx_massage_active_display_order
    ON massage (active, display_order);

CREATE INDEX idx_massage_option_massage_active
    ON massage_option (massage_id, active, display_order);

CREATE INDEX idx_booking_appointment_date
    ON booking (appointment_date);

CREATE INDEX idx_booking_schedule
    ON booking (appointment_date, start_time, end_time);

CREATE INDEX idx_booking_status
    ON booking (status);

CREATE TABLE notification_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL,
    provider_message_id VARCHAR(255),
    error_message TEXT,
    sent_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_log_booking
        FOREIGN KEY (booking_id)
            REFERENCES booking(id)
            ON DELETE CASCADE,

    CONSTRAINT chk_notification_log_channel
        CHECK (channel IN ('EMAIL', 'SMS')),

    CONSTRAINT chk_notification_log_status
        CHECK (status IN ('PENDING', 'SENT', 'FAILED'))
);