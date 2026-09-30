import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AdminBookingNoteUpdateRequest,
  AdminBookingStatusUpdateRequest,
  AvailableSlot,
  Booking,
  BookingCreatedResponse,
  BookingStatus,
  CancelBookingRequest,
  CreateBookingRequest,
  CustomerBookingRescheduleRequest,
  LogicResult,
} from '../models/booking.model';

@Injectable({
  providedIn: 'root',
})
export class BookingService {
  private readonly publicApiUrl = `${environment.apiUrl}/bookings`;
  private readonly adminApiUrl = `${environment.adminApiUrl}/bookings`;

  constructor(private readonly http: HttpClient) {}

  getAvailableSlots(massageOptionId: number, date: string): Observable<AvailableSlot[]> {
    const params = new HttpParams()
      .set('massageOptionId', massageOptionId)
      .set('date', date);

    return this.http.get<AvailableSlot[]>(`${this.publicApiUrl}/available-slots`, { params });
  }

  createBooking(request: CreateBookingRequest): Observable<BookingCreatedResponse> {
    return this.http
      .post<LogicResult<BookingCreatedResponse>>(this.publicApiUrl, request)
      .pipe(map((response) => response.data));
  }

  getManagedBooking(token: string): Observable<Booking> {
    return this.http.get<Booking>(`${this.publicApiUrl}/manage/${encodeURIComponent(token)}`);
  }

  rescheduleManagedBooking(
    token: string,
    request: CustomerBookingRescheduleRequest,
  ): Observable<BookingCreatedResponse> {
    return this.http
      .patch<LogicResult<BookingCreatedResponse>>(
        `${this.publicApiUrl}/manage/${encodeURIComponent(token)}/reschedule`,
        request,
      )
      .pipe(map((response) => response.data));
  }

  cancelManagedBooking(token: string, request: CancelBookingRequest): Observable<Booking> {
    return this.http
      .patch<LogicResult<Booking>>(
        `${this.publicApiUrl}/manage/${encodeURIComponent(token)}/cancel`,
        request,
      )
      .pipe(map((response) => response.data));
  }

  getAdminBookings(filters: {
    from?: string | null;
    to?: string | null;
    status?: BookingStatus | '' | null;
  }): Observable<Booking[]> {
    let params = new HttpParams();

    if (filters.from) {
      params = params.set('from', filters.from);
    }

    if (filters.to) {
      params = params.set('to', filters.to);
    }

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    return this.http.get<Booking[]>(this.adminApiUrl, { params });
  }

  getAdminBooking(id: number): Observable<Booking> {
    return this.http.get<Booking>(`${this.adminApiUrl}/${id}`);
  }

  updateAdminBookingStatus(
    id: number,
    request: AdminBookingStatusUpdateRequest,
  ): Observable<Booking> {
    return this.http
      .patch<LogicResult<Booking>>(`${this.adminApiUrl}/${id}/status`, request)
      .pipe(map((response) => response.data));
  }

  updateAdminBookingNote(id: number, request: AdminBookingNoteUpdateRequest): Observable<Booking> {
    return this.http
      .patch<LogicResult<Booking>>(`${this.adminApiUrl}/${id}/note`, request)
      .pipe(map((response) => response.data));
  }

  cancelAdminBooking(id: number, request: CancelBookingRequest): Observable<Booking> {
    return this.http
      .patch<LogicResult<Booking>>(`${this.adminApiUrl}/${id}/cancel`, request)
      .pipe(map((response) => response.data));
  }
}
