import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, tap, map } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest } from '../models/auth.model';
import { LogicResult } from '../models/logic-result.model';

const ADMIN_TOKEN_KEY = 'mmd_admin_access_token';
const ADMIN_USER_KEY = 'mmd_admin_user';

@Injectable({
  providedIn: 'root',
})
export class AdminAuthService {
  private readonly http = inject(HttpClient);
  private readonly authApiUrl = environment.authApiUrl;
  private readonly tokenSignal = signal<string | null>(this.readToken());
  private readonly userSignal = signal<AuthResponse['user'] | null>(this.readUser());

  readonly currentUser = computed(() => this.userSignal());
  readonly isAuthenticated = computed(() => {
    const token = this.tokenSignal();
      if (!token) {
        return false;
      }
      return !this.isTokenExpired(token);
    });

  get token(): string | null {
    return this.tokenSignal();
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<LogicResult<AuthResponse>>(`${this.authApiUrl}/login`, {
        email: request.email.trim(),
        password: request.password,
      })
      .pipe(
        map((response) => response.data),
        tap((response) => this.persistSession(response)),
      );
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http
      .post<LogicResult<AuthResponse>>(`${this.authApiUrl}/register`, {
        email: request.email.trim(),
        password: request.password,
        fullName: request.fullName.trim(),
        invitationCode: request.invitationCode.trim(),
      })
      .pipe(
        map((response) => response.data),
        tap((response) => this.persistSession(response)),
      );
  }

  private isTokenExpired(token: string): boolean {
    try {
      const parts = token.split('.');

      if (parts.length !== 3) {
        return true;
      }

      const payload = JSON.parse(
        atob(parts[1].replaceAll('-', '+').replaceAll('_', '/'))
      ) as { exp?: number };

      if (typeof payload.exp !== 'number') {
        return true;
      }

      return payload.exp * 1000 <= Date.now();
    } catch {
      return true;
    }
  }

  private persistSession(response: AuthResponse): void {
    this.setToken(response.accessToken);
    sessionStorage.setItem(ADMIN_USER_KEY, JSON.stringify(response.user));
    this.userSignal.set(response.user);
  }

  private setToken(token: string): void {
    const trimmedToken = token.trim();

    if (!trimmedToken) {
      this.clearToken();
      return;
    }

    sessionStorage.setItem(ADMIN_TOKEN_KEY, trimmedToken);
    this.tokenSignal.set(trimmedToken);
  }

  clearToken(): void {
    sessionStorage.removeItem(ADMIN_TOKEN_KEY);
    sessionStorage.removeItem(ADMIN_USER_KEY);
    this.tokenSignal.set(null);
    this.userSignal.set(null);
  }

  private readToken(): string | null {
    if (typeof sessionStorage === 'undefined') {
      return null;
    }

    return sessionStorage.getItem(ADMIN_TOKEN_KEY);
  }

  private readUser(): AuthResponse['user'] | null {
    if (typeof sessionStorage === 'undefined') {
      return null;
    }

    const serializedUser = sessionStorage.getItem(ADMIN_USER_KEY);

    if (!serializedUser) {
      return null;
    }

    try {
      return JSON.parse(serializedUser) as AuthResponse['user'];
    } catch {
      sessionStorage.removeItem(ADMIN_USER_KEY);
      return null;
    }
  }
}
