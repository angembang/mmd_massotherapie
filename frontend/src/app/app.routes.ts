import { Routes } from '@angular/router';
import {HomeComponent} from './features/home/home/home.component';
import { adminAuthGuard } from './guards/admin-auth.guard';

export const routes: Routes = [
  { path: "", component: HomeComponent},
  {
    path: 'booking',
    loadComponent: () =>
      import('./features/booking/booking-page/booking-page.component').then(
        (component) => component.BookingPageComponent,
      ),
  },
  {
    path: 'bookings/manage/:token',
    loadComponent: () =>
      import('./features/booking/booking-manage/booking-manage.component').then(
        (component) => component.BookingManageComponent,
      ),
  },
  {
    path: 'admin',
    loadComponent: () =>
      import('./features/admin/admin-login/admin-login.component').then(
        (component) => component.AdminLoginComponent,
      ),
  },
  {
    path: 'admin/register',
    loadComponent: () =>
      import('./features/admin/admin-register/admin-register.component').then(
        (component) => component.AdminRegisterComponent,
      ),
  },
  {
    path: 'admin/bookings',
    canActivate: [adminAuthGuard],
    loadComponent: () =>
      import('./features/admin/admin-bookings/admin-bookings.component').then(
        (component) => component.AdminBookingsComponent,
      ),
  },
  { path: '**', redirectTo: '' }
];
