import { Routes } from '@angular/router';
import {HomeComponent} from './features/home/home/home.component';

export const routes: Routes = [
  { path: "", component: HomeComponent},

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
  { path: '**', redirectTo: '' }
];
