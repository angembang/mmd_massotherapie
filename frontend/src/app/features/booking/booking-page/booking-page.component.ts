import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { BookingCreatedResponse, AvailableSlot } from '../../../models/booking.model';
import { Massage, MassageOption } from '../../../models/massage.model';
import { BookingService } from '../../../services/booking.service';
import { MassageService } from '../../../services/massage.service';

@Component({
  selector: 'app-booking-page',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './booking-page.component.html',
  styleUrl: './booking-page.component.scss',
})
export class BookingPageComponent {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly massageService = inject(MassageService);
  private readonly bookingService = inject(BookingService);
  private readonly preselectedOptionId = Number(this.route.snapshot.queryParamMap.get('optionId') ?? 0);

  readonly massages = signal<Massage[]>([]);
  readonly slots = signal<AvailableSlot[]>([]);
  readonly loadingMassages = signal(false);
  readonly loadingSlots = signal(false);
  readonly submitting = signal(false);
  readonly error = signal<string | null>(null);
  readonly success = signal<BookingCreatedResponse | null>(null);

  readonly bookingForm = this.fb.nonNullable.group({
    massageId: [0, [Validators.required, Validators.min(1)]],
    massageOptionId: [0, [Validators.required, Validators.min(1)]],
    appointmentDate: ['', Validators.required],
    startTime: ['', Validators.required],
    customerName: ['', [Validators.required, Validators.maxLength(150)]],
    customerEmail: ['', [Validators.email, Validators.maxLength(150)]],
    customerPhone: ['', Validators.maxLength(30)],
    customerMessage: ['', Validators.maxLength(2000)],
  });

  constructor() {
    this.loadMassages();

    this.bookingForm.controls.massageId.valueChanges.subscribe(() => {
      this.bookingForm.patchValue({ massageOptionId: 0, startTime: '' }, { emitEvent: false });
      this.slots.set([]);
    });

    this.bookingForm.controls.massageOptionId.valueChanges.subscribe(() => this.loadSlots());
    this.bookingForm.controls.appointmentDate.valueChanges.subscribe(() => this.loadSlots());
  }

  submit(): void {
    this.error.set(null);
    this.success.set(null);

    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      this.error.set('Merci de compléter les champs obligatoires.');
      return;
    }

    const value = this.bookingForm.getRawValue();

    if (!value.customerEmail.trim() && !value.customerPhone.trim()) {
      this.error.set('Merci de renseigner un email ou un téléphone.');
      return;
    }

    this.submitting.set(true);

    this.bookingService
      .createBooking({
        customerName: value.customerName.trim(),
        customerEmail: value.customerEmail.trim() || null,
        customerPhone: value.customerPhone.trim() || null,
        massageOptionId: value.massageOptionId,
        appointmentDate: value.appointmentDate,
        startTime: value.startTime,
        customerMessage: value.customerMessage.trim() || null,
      })
      .pipe(finalize(() => this.submitting.set(false)))
      .subscribe({
        next: (response) => {
          this.success.set(response);
          this.bookingForm.reset({
            massageId: 0,
            massageOptionId: 0,
            appointmentDate: '',
            startTime: '',
            customerName: '',
            customerEmail: '',
            customerPhone: '',
            customerMessage: '',
          });
          this.slots.set([]);
        },
        error: () => this.error.set('Ce créneau n’est plus disponible ou les informations sont invalides.'),
      });
  }

  formatPrice(priceCents: number): string {
    return `${priceCents / 100} EUR`;
  }

  formatOption(option: MassageOption): string {
    const duration = option.durationMinutes < 60
      ? `${option.durationMinutes} min`
      : `${Math.floor(option.durationMinutes / 60)}h${option.durationMinutes % 60 || ''}`;

    const bodyArea = option.bodyArea === 'FULL_BODY'
      ? 'Corps complet'
      : option.bodyArea === 'UPPER_OR_LOWER_BODY'
        ? 'Haut/bas du corps'
        : 'Séance';

    return `${bodyArea} - ${duration} - ${this.formatPrice(option.priceCents)}`;
  }

  selectedOptions(): MassageOption[] {
    return this.massages().find(
      (massage) => massage.id === this.bookingForm.controls.massageId.value,
    )?.options ?? [];
  }

  private loadMassages(): void {
    this.loadingMassages.set(true);

    this.massageService
      .getMassages()
      .pipe(finalize(() => this.loadingMassages.set(false)))
      .subscribe({
        next: (massages) => {
          this.massages.set(massages);
          this.applyPreselectedOption();
        },
        error: () => this.error.set('Impossible de charger les prestations.'),
      });
  }

  private applyPreselectedOption(): void {
    if (!this.preselectedOptionId) {
      return;
    }

    const massage = this.massages().find((item) =>
      item.options.some((option) => option.id === this.preselectedOptionId),
    );

    if (!massage) {
      return;
    }

    this.bookingForm.patchValue({
      massageId: massage.id,
      massageOptionId: this.preselectedOptionId,
    });
  }

  private loadSlots(): void {
    const optionId = this.bookingForm.controls.massageOptionId.value;
    const date = this.bookingForm.controls.appointmentDate.value;

    this.bookingForm.patchValue({ startTime: '' }, { emitEvent: false });
    this.slots.set([]);

    if (!optionId || !date) {
      return;
    }

    this.loadingSlots.set(true);

    this.bookingService
      .getAvailableSlots(optionId, date)
      .pipe(finalize(() => this.loadingSlots.set(false)))
      .subscribe({
        next: (slots) => this.slots.set(slots),
        error: () => this.error.set('Impossible de charger les créneaux disponibles.'),
      });
  }
}
