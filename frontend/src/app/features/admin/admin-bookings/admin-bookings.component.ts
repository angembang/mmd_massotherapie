import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { finalize } from 'rxjs';
import { Booking, BookingStatus } from '../../../models/booking.model';
import { AdminAuthService } from '../../../services/admin-auth.service';
import { BookingService } from '../../../services/booking.service';

@Component({
  selector: 'app-admin-bookings',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-bookings.component.html',
  styleUrl: './admin-bookings.component.scss',
})
export class AdminBookingsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly authService = inject(AdminAuthService);
  private readonly bookingService = inject(BookingService);

  readonly bookings = signal<Booking[]>([]);
  readonly selectedBooking = signal<Booking | null>(null);
  readonly loading = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly message = signal<string | null>(null);
  readonly statuses: BookingStatus[] = ['CONFIRMED', 'COMPLETED', 'CANCELLED'];

  readonly filtersForm = this.fb.nonNullable.group({
    from: [''],
    to: [''],
    status: ['' as BookingStatus | ''],
  });

  readonly noteForm = this.fb.nonNullable.group({
    internalNote: [''],
  });

  readonly statusForm = this.fb.nonNullable.group({
    status: ['CONFIRMED' as BookingStatus],
    internalNote: [''],
  });

  readonly cancelForm = this.fb.nonNullable.group({
    reason: [''],
  });

  constructor() {
    this.loadBookings();
  }

  loadBookings(): void {
    this.loading.set(true);
    this.error.set(null);

    this.bookingService
      .getAdminBookings(this.filtersForm.getRawValue())
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (bookings) => this.bookings.set(bookings),
        error: () => this.error.set('Impossible de charger les rendez-vous admin.'),
      });
  }

  selectBooking(booking: Booking): void {
    this.selectedBooking.set(booking);
    this.noteForm.setValue({ internalNote: booking.internalNote ?? '' });
    this.statusForm.setValue({ status: booking.status, internalNote: booking.internalNote ?? '' });
    this.cancelForm.setValue({ reason: booking.cancellationReason ?? '' });
  }

  saveNote(): void {
    const booking = this.selectedBooking();

    if (!booking) {
      return;
    }

    this.saving.set(true);
    this.message.set(null);

    this.bookingService
      .updateAdminBookingNote(booking.id, this.noteForm.getRawValue())
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (updatedBooking) => this.afterUpdate(updatedBooking, 'Note enregistrée.'),
        error: () => this.error.set('Impossible d’enregistrer la note.'),
      });
  }

  updateStatus(): void {
    const booking = this.selectedBooking();

    if (!booking) {
      return;
    }

    this.saving.set(true);
    this.message.set(null);

    this.bookingService
      .updateAdminBookingStatus(booking.id, this.statusForm.getRawValue())
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (updatedBooking) => this.afterUpdate(updatedBooking, 'Statut mis à jour.'),
        error: () => this.error.set('Impossible de mettre à jour le statut.'),
      });
  }

  cancelBooking(): void {
    const booking = this.selectedBooking();

    if (!booking) {
      return;
    }

    this.saving.set(true);
    this.message.set(null);

    this.bookingService
      .cancelAdminBooking(booking.id, {
        reason: this.cancelForm.controls.reason.value.trim() || null,
      })
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (updatedBooking) => this.afterUpdate(updatedBooking, 'Rendez-vous annulé.'),
        error: () => this.error.set('Impossible d’annuler ce rendez-vous.'),
      });
  }

  logout(): void {
    this.authService.clearToken();
    void this.router.navigateByUrl('/admin');
  }

  private afterUpdate(updatedBooking: Booking, message: string): void {
    this.selectedBooking.set(updatedBooking);
    this.bookings.update((bookings) =>
      bookings.map((booking) => (booking.id === updatedBooking.id ? updatedBooking : booking)),
    );
    this.message.set(message);
  }
}
