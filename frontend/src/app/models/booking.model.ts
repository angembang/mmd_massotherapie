import { BodyArea } from './massage.model';

export type BookingStatus = 'PENDING' | 'CONFIRMED' | 'CANCELLED' | 'COMPLETED';

export type BookingCancellationActor = 'CUSTOMER' | 'ADMIN';

export interface AvailableSlot {
  startTime: string;
  endTime: string;
}

export interface CreateBookingRequest {
  customerName: string;
  customerEmail?: string | null;
  customerPhone?: string | null;
  massageOptionId: number;
  appointmentDate: string;
  startTime: string;
  customerMessage?: string | null;
}

export interface Booking {
  id: number;
  customerName: string;
  customerEmail?: string | null;
  customerPhone?: string | null;
  massageOptionId: number;
  massageName: string;
  durationMinutes: number;
  bodyArea: BodyArea;
  priceCents: number;
  appointmentDate: string;
  startTime: string;
  endTime: string;
  status: BookingStatus;
  customerMessage?: string | null;
  internalNote?: string | null;
  cancellationReason?: string | null;
  cancelledBy?: BookingCancellationActor | null;
  cancelledAt?: string | null;
  createdAt?: string | null;
  updatedAt?: string | null;
}

export interface BookingCreatedResponse {
  booking: Booking;
  managementUrl: string;
}

export interface LogicResult<T> {
  code: string;
  message: string;
  data: T;
}

export interface CustomerBookingRescheduleRequest {
  appointmentDate: string;
  startTime: string;
}

export interface CancelBookingRequest {
  reason?: string | null;
}

export interface AdminBookingStatusUpdateRequest {
  status: BookingStatus;
  internalNote?: string | null;
}

export interface AdminBookingNoteUpdateRequest {
  internalNote?: string | null;
}
