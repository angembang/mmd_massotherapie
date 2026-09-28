import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { finalize } from 'rxjs';
import { AdminAuthService } from '../../../services/admin-auth.service';

@Component({
  selector: 'app-admin-register',
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './admin-register.component.html',
  styleUrl: './admin-register.component.scss',
})
export class AdminRegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly router = inject(Router);
  private readonly authService = inject(AdminAuthService);

  readonly error = signal<string | null>(null);
  readonly loading = signal(false);
  readonly registerForm = this.fb.nonNullable.group({
    fullName: ['', [Validators.required, Validators.maxLength(150)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    password: ['', [Validators.required, Validators.minLength(12), Validators.maxLength(100)]],
    invitationCode: ['', [Validators.required, Validators.maxLength(255)]],
  });

  submit(): void {
    this.error.set(null);

    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      this.error.set(this.getFormValidationMessage());
      return;
    }

    const password = this.registerForm.controls.password.value;

    if (!this.isStrongPassword(password)) {
      this.error.set('Le mot de passe doit contenir majuscule, minuscule, chiffre et caractere special.');
      return;
    }

    this.loading.set(true);

    this.authService
      .register(this.registerForm.getRawValue())
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => void this.router.navigateByUrl('/admin/bookings'),
        error: () => this.error.set('Creation impossible. Verifiez le code d’invitation et les informations.'),
      });
  }

  private isStrongPassword(password: string): boolean {
    return /[A-Z]/.test(password)
      && /[a-z]/.test(password)
      && /\d/.test(password)
      && /[^a-zA-Z0-9]/.test(password);
  }

  private getFormValidationMessage(): string {
    const controls = this.registerForm.controls;

    if (controls.fullName.invalid) {
      return 'Le nom complet est requis.';
    }

    if (controls.email.hasError('required')) {
      return 'L’email est requis.';
    }

    if (controls.email.hasError('email')) {
      return 'L’email n’est pas valide.';
    }

    if (controls.password.hasError('required')) {
      return 'Le mot de passe est requis.';
    }

    if (controls.password.hasError('minlength')) {
      return 'Le mot de passe doit contenir au moins 12 caracteres.';
    }

    if (controls.invitationCode.invalid) {
      return 'Le code d’invitation est requis.';
    }

    return 'Merci de verifier les informations saisies.';
  }
}
