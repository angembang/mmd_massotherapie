import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AvailableSlot, Booking } from '../../../models/booking.model';
import { BookingService } from '../../../services/booking.service';

@Component({
  selector: 'app-booking-manage',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './booking-manage.component.html',
  styleUrl: './booking-manage.component.scss',
})
export class BookingManageComponent {
  private readonly route = inject(ActivatedRoute);
  private readonly fb = inject(FormBuilder);
  private readonly bookingService = inject(BookingService);
  private readonly token = this.route.snapshot.paramMap.get('token') ?? '';

  readonly booking = signal<Booking | null>(null);
  readonly slots = signal<AvailableSlot[]>([]);
  readonly loading = signal(false);
  readonly loadingSlots = signal(false);
  readonly saving = signal(false);
  readonly error = signal<string | null>(null);
  readonly message = signal<string | null>(null);

  readonly rescheduleForm = this.fb.nonNullable.group({
    appointmentDate: ['', Validators.required],
    startTime: ['', Validators.required],
  });

  readonly cancelForm = this.fb.nonNullable.group({
    reason: ['', Validators.maxLength(2000)],
  });

  constructor() {
    this.loadBooking();
    this.rescheduleForm.controls.appointmentDate.valueChanges.subscribe(() => this.loadSlots());
  }

  reschedule(): void {
    const booking = this.booking();

    if (!booking || this.rescheduleForm.invalid) {
      this.rescheduleForm.markAllAsTouched();
      return;
    }

    this.saving.set(true);
    this.error.set(null);
    this.message.set(null);

    this.bookingService
      .rescheduleManagedBooking(this.token, this.rescheduleForm.getRawValue())
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (response) => {
          this.booking.set(response.booking);
          this.message.set(this.notificationMessage(response.booking, 'Votre rendez-vous a été modifié.'));
          this.rescheduleForm.reset({ appointmentDate: '', startTime: '' });
          this.slots.set([]);
        },
        error: () => this.error.set('Impossible de modifier ce rendez-vous.'),
      });
  }

  cancel(): void {
    this.saving.set(true);
    this.error.set(null);
    this.message.set(null);

    this.bookingService
      .cancelManagedBooking(this.token, {
        reason: this.cancelForm.controls.reason.value.trim() || null,
      })
      .pipe(finalize(() => this.saving.set(false)))
      .subscribe({
        next: (booking) => {
          this.booking.set(booking);
          this.message.set(this.notificationMessage(booking, 'Votre rendez-vous a été annulé.'));
        },
        error: () => this.error.set("Impossible d'annuler ce rendez-vous."),
      });
  }

  private loadBooking(): void {
    this.loading.set(true);
    this.error.set(null);

    this.bookingService
      .getManagedBooking(this.token)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: (booking) => this.booking.set(booking),
        error: () => this.error.set('Le lien de gestion est invalide ou expire.'),
      });
  }

  private loadSlots(): void {
    const booking = this.booking();
    const date = this.rescheduleForm.controls.appointmentDate.value;

    this.rescheduleForm.patchValue({ startTime: '' }, { emitEvent: false });
    this.slots.set([]);

    if (!booking || !date) {
      return;
    }

    this.loadingSlots.set(true);

    this.bookingService
      .getAvailableSlots(booking.massageOptionId, date)
      .pipe(finalize(() => this.loadingSlots.set(false)))
      .subscribe({
        next: (slots) => this.slots.set(slots),
        error: () => this.error.set('Impossible de charger les créneaux disponibles.'),
      });
  }

  private notificationMessage(booking: Booking, baseMessage: string): string {
    if (!booking.customerEmail) {
      return `${baseMessage} Aucun email n'a été renseigné.`;
    }

    return `${baseMessage} Un email de confirmation a été envoyé à ${booking.customerEmail}.`;
  }
}
