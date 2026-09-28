import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { AdminAuthService } from './admin-auth.service';

export const adminAuthInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  const authService = inject(AdminAuthService);
  const adminApiPrefix = environment.adminApiUrl;
  const token = authService.token;

  if (!token || !request.url.startsWith(adminApiPrefix)) {
    return next(request);
  }

  const authenticatedRequest = request.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authenticatedRequest).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.clearToken();
        void router.navigateByUrl('/admin');
      }

      return throwError(() => error);
    }),
  );
};
