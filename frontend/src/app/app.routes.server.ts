import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    path: '',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'booking',
    renderMode: RenderMode.Prerender
  },
  {
    path: 'bookings/manage/:token',
    renderMode: RenderMode.Server
  },
  {
    path: 'admin',
    renderMode: RenderMode.Server
  },
  {
    path: 'admin/register',
    renderMode: RenderMode.Server
  },
  {
    path: 'admin/bookings',
    renderMode: RenderMode.Server
  },
  {
    path: '**',
    renderMode: RenderMode.Server
  }
];
