package fr.mmdmassotherapie.backend.booking.service;

import fr.mmdmassotherapie.backend.booking.dto.CreateBookingRequest;
import fr.mmdmassotherapie.backend.booking.dao.IDAOBooking;
import fr.mmdmassotherapie.backend.booking.dto.*;
import fr.mmdmassotherapie.backend.booking.exception.BookingConflictException;
import fr.mmdmassotherapie.backend.booking.exception.BookingNotFoundException;
import fr.mmdmassotherapie.backend.booking.exception.BookingTokenException;
import fr.mmdmassotherapie.backend.booking.exception.BookingValidationException;
import fr.mmdmassotherapie.backend.booking.model.*;
import fr.mmdmassotherapie.backend.massage.dao.IDAOMassage;
import fr.mmdmassotherapie.backend.massage.model.MassageOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {
    private static final int DEFAULT_ADMIN_RANGE_DAYS = 30;

    private final IDAOBooking bookingDAO;
    private final IDAOMassage massageDAO;
    private final BookingPublicTokenService publicTokenService;
    private final BookingNotificationService notificationService;

    @Value("${app.booking.slot-step-minutes}")
    private int slotStepMinutes;

    public BookingService(
            IDAOBooking bookingDAO,
            IDAOMassage massageDAO,
            BookingPublicTokenService publicTokenService,
            BookingNotificationService notificationService
    ) {
        this.bookingDAO = bookingDAO;
        this.massageDAO = massageDAO;
        this.publicTokenService = publicTokenService;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<AvailableSlotResponse> getAvailableSlots(Long massageOptionId, LocalDate date) {
        if (date == null) {
            throw new BookingValidationException("Appointment date is required");
        }

        if (date.isBefore(LocalDate.now())) {
            return List.of();
        }

        MassageOption massageOption = findActiveMassageOption(massageOptionId);

        if (bookingDAO.isClosed(date)) {
            return List.of();
        }

        WorkingDay workingDay = bookingDAO.findActiveWorkingDay(date.getDayOfWeek())
                .orElse(null);

        if (workingDay == null) {
            return List.of();
        }

        List<Booking> blockingBookings = bookingDAO.findBlockingBookings(date);
        LocalTime latestStartTime = workingDay.getClosingTime().minusMinutes(massageOption.getDurationMinutes());

        return generateSlots(workingDay.getOpeningTime(), latestStartTime)
                .stream()
                .filter(startTime -> date.isAfter(LocalDate.now()) || startTime.isAfter(LocalTime.now()))
                .map(startTime -> new AvailableSlotResponse(
                        startTime,
                        startTime.plusMinutes(massageOption.getDurationMinutes())
                ))
                .filter(slot -> blockingBookings.stream()
                        .noneMatch(booking -> overlaps(
                                slot.startTime(),
                                slot.endTime(),
                                booking.getStartTime(),
                                booking.getEndTime()
                        ))
                )
                .toList();
    }

    @Transactional
    public BookingCreatedResponse createBooking(CreateBookingRequest request) {
        String customerName = requiredTrim(request.customerName(), "Customer name is required");
        String customerEmail = nullableTrim(request.customerEmail());
        String customerPhone = nullableTrim(request.customerPhone());
        String customerMessage = nullableTrim(request.customerMessage());

        if (customerEmail == null && customerPhone == null) {
            throw new BookingValidationException("Email or phone is required");
        }

        MassageOption massageOption = findActiveMassageOption(request.massageOptionId());
        LocalDate appointmentDate = request.appointmentDate();
        LocalTime startTime = request.startTime();
        LocalTime endTime = calculateEndTime(startTime, massageOption.getDurationMinutes());

        validateAvailability(appointmentDate, startTime, endTime, null);

        Booking booking = new Booking(
                customerName,
                customerEmail,
                customerPhone,
                massageOption,
                appointmentDate,
                startTime,
                endTime,
                customerMessage
        );

        String publicToken = publicTokenService.generateToken();
        booking.setPublicManagementToken(
                publicTokenService.hashToken(publicToken),
                publicTokenService.expiresAt()
        );

        Booking savedBooking = bookingDAO.save(booking);
        String managementUrl = publicTokenService.managementUrl(publicToken);
        notificationService.sendBookingConfirmation(savedBooking, managementUrl);

        return new BookingCreatedResponse(toPublicResponse(savedBooking), managementUrl);
    }

    @Transactional(readOnly = true)
    public PublicBookingResponse getPublicBooking(String token) {
        return toPublicResponse(findBookingByValidToken(token));
    }

    @Transactional
    public BookingCreatedResponse reschedulePublicBooking(String token, CustomerBookingRescheduleRequest request) {
        Booking booking = findBookingByValidToken(token);
        ensureCustomerCanManage(booking);

        LocalDate appointmentDate = request.appointmentDate();
        LocalTime startTime = request.startTime();
        LocalTime endTime = calculateEndTime(startTime, booking.getDurationMinutesSnapshot());

        validateAvailability(appointmentDate, startTime, endTime, booking.getId());
        booking.reschedule(appointmentDate, startTime, endTime);

        Booking savedBooking = bookingDAO.save(booking);
        String managementUrl = publicTokenService.managementUrl(token);
        notificationService.sendBookingRescheduled(savedBooking, managementUrl);

        return new BookingCreatedResponse(toPublicResponse(savedBooking), managementUrl);
    }

    @Transactional
    public PublicBookingResponse cancelPublicBooking(String token, CancelBookingRequest request) {
        Booking booking = findBookingByValidToken(token);
        ensureCustomerCanManage(booking);

        booking.cancel(BookingCancellationActor.CUSTOMER, nullableTrim(request.reason()));
        Booking savedBooking = bookingDAO.save(booking);
        notificationService.sendCustomerCancellationConfirmation(savedBooking);

        return toPublicResponse(savedBooking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getAdminBookings(LocalDate from, LocalDate to, BookingStatus status) {
        LocalDate resolvedFrom = from == null ? LocalDate.now() : from;
        LocalDate resolvedTo = to == null ? resolvedFrom.plusDays(DEFAULT_ADMIN_RANGE_DAYS) : to;

        if (resolvedTo.isBefore(resolvedFrom)) {
            throw new BookingValidationException("End date must be after or equal to start date");
        }

        return bookingDAO.findBookings(resolvedFrom, resolvedTo, status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookingResponse getAdminBooking(Long id) {
        return toResponse(findBooking(id));
    }

    @Transactional
    public BookingResponse updateStatus(Long id, AdminBookingStatusUpdateRequest request) {
        if (request.status() == BookingStatus.CANCELLED) {
            return cancelAdminBooking(id, new CancelBookingRequest(request.internalNote()));
        }

        Booking booking = findBooking(id);
        BookingStatus requestedStatus = request.status();

        if (!canTransition(booking.getStatus(), requestedStatus)) {
            throw new BookingValidationException(
                    "Cannot change booking status from " + booking.getStatus() + " to " + requestedStatus
            );
        }

        booking.updateStatus(requestedStatus, nullableTrim(request.internalNote()));
        return toResponse(bookingDAO.save(booking));
    }

    @Transactional
    public BookingResponse updateInternalNote(Long id, AdminBookingNoteUpdateRequest request) {
        Booking booking = findBooking(id);
        booking.updateInternalNote(nullableTrim(request.internalNote()));

        return toResponse(bookingDAO.save(booking));
    }

    @Transactional
    public BookingResponse cancelAdminBooking(Long id, CancelBookingRequest request) {
        Booking booking = findBooking(id);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            return toResponse(booking);
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingValidationException("Completed booking cannot be cancelled");
        }

        String reason = nullableTrim(request.reason());
        booking.cancel(BookingCancellationActor.ADMIN, reason);

        Booking savedBooking = bookingDAO.save(booking);
        notificationService.sendAdminCancellationNotice(savedBooking, reason);

        return toResponse(savedBooking);
    }

    private MassageOption findActiveMassageOption(Long massageOptionId) {
        return massageDAO.findActiveOptionById(massageOptionId)
                .orElseThrow(() -> new BookingValidationException("Massage option is not available"));
    }

    private Booking findBooking(Long id) {
        return bookingDAO.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    private Booking findBookingByValidToken(String token) {
        String tokenHash = publicTokenService.hashToken(token);

        Booking booking = bookingDAO.findByPublicTokenHash(tokenHash)
                .orElseThrow(BookingTokenException::new);

        if (booking.getPublicTokenExpiresAt() == null || booking.getPublicTokenExpiresAt().isBefore(Instant.now())) {
            throw new BookingTokenException();
        }

        return booking;
    }

    private void ensureCustomerCanManage(Booking booking) {
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingValidationException("Booking can no longer be managed");
        }

        if (booking.getAppointmentDate().isBefore(LocalDate.now())
                || booking.getAppointmentDate().isEqual(LocalDate.now()) && !booking.getStartTime().isAfter(LocalTime.now())) {
            throw new BookingValidationException("Past booking can no longer be managed");
        }
    }

    private void validateAvailability(
            LocalDate appointmentDate,
            LocalTime startTime,
            LocalTime endTime,
            Long excludedBookingId
    ) {
        if (appointmentDate.isBefore(LocalDate.now())) {
            throw new BookingValidationException("Appointment date cannot be in the past");
        }

        if (appointmentDate.isEqual(LocalDate.now()) && !startTime.isAfter(LocalTime.now())) {
            throw new BookingValidationException("Appointment start time must be in the future");
        }

        if (bookingDAO.isClosed(appointmentDate)) {
            throw new BookingConflictException("Business is closed on this date");
        }

        WorkingDay workingDay = bookingDAO.findActiveWorkingDay(appointmentDate.getDayOfWeek())
                .orElseThrow(() -> new BookingConflictException("Business is not open on this day"));

        if (startTime.isBefore(workingDay.getOpeningTime()) || endTime.isAfter(workingDay.getClosingTime())) {
            throw new BookingConflictException("Appointment is outside business opening hours");
        }

        boolean hasBlockingBooking = excludedBookingId == null
                ? bookingDAO.hasBlockingBooking(appointmentDate, startTime, endTime)
                : bookingDAO.hasBlockingBookingExcluding(excludedBookingId, appointmentDate, startTime, endTime);

        if (hasBlockingBooking) {
            throw new BookingConflictException("Appointment slot is already booked");
        }
    }

    private LocalTime calculateEndTime(LocalTime startTime, int durationMinutes) {
        LocalTime endTime = startTime.plusMinutes(durationMinutes);

        if (!endTime.isAfter(startTime)) {
            throw new BookingValidationException("Appointment cannot end on the next day");
        }

        return endTime;
    }

    private boolean canTransition(BookingStatus currentStatus, BookingStatus requestedStatus) {
        if (currentStatus == requestedStatus) {
            return true;
        }

        return switch (currentStatus) {
            case PENDING -> requestedStatus == BookingStatus.CONFIRMED
                    || requestedStatus == BookingStatus.CANCELLED;
            case CONFIRMED -> requestedStatus == BookingStatus.COMPLETED
                    || requestedStatus == BookingStatus.CANCELLED;
            case CANCELLED, COMPLETED -> false;
        };
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getCustomerPhone(),
                booking.getMassageOption().getId(),
                booking.getMassageNameSnapshot(),
                booking.getDurationMinutesSnapshot(),
                booking.getBodyAreaSnapshot(),
                booking.getPriceCentsSnapshot(),
                booking.getAppointmentDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getCustomerMessage(),
                booking.getInternalNote(),
                booking.getCancellationReason(),
                booking.getCancelledBy(),
                booking.getCancelledAt(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }

    private PublicBookingResponse toPublicResponse(Booking booking) {
        return new PublicBookingResponse(
                booking.getId(),
                booking.getCustomerName(),
                booking.getCustomerEmail(),
                booking.getCustomerPhone(),
                booking.getMassageOption().getId(),
                booking.getMassageNameSnapshot(),
                booking.getDurationMinutesSnapshot(),
                booking.getBodyAreaSnapshot(),
                booking.getPriceCentsSnapshot(),
                booking.getAppointmentDate(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getStatus(),
                booking.getCustomerMessage(),
                booking.getCancellationReason(),
                booking.getCancelledBy(),
                booking.getCancelledAt(),
                booking.getCreatedAt(),
                booking.getUpdatedAt()
        );
    }

    private List<LocalTime> generateSlots(LocalTime openingTime, LocalTime latestStartTime) {
        if (slotStepMinutes <= 0) {
            throw new BookingValidationException("Slot step minutes must be greater than 0");
        }

        if (latestStartTime.isBefore(openingTime)) {
            return List.of();
        }

        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = openingTime;

        while (!current.isAfter(latestStartTime)) {
            slots.add(current);
            current = current.plusMinutes(slotStepMinutes);
        }

        return List.copyOf(slots);
    }

    private boolean overlaps(
            LocalTime requestedStartTime,
            LocalTime requestedEndTime,
            LocalTime bookedStartTime,
            LocalTime bookedEndTime
    ) {
        return requestedStartTime.isBefore(bookedEndTime) && requestedEndTime.isAfter(bookedStartTime);
    }

    private String requiredTrim(String value, String message) {
        String trimmedValue = nullableTrim(value);

        if (trimmedValue == null) {
            throw new BookingValidationException(message);
        }

        return trimmedValue;
    }

    private String nullableTrim(String value) {
        if (value == null) {
            return null;
        }

        String trimmedValue = value.trim();
        return trimmedValue.isBlank() ? null : trimmedValue;
    }
}
