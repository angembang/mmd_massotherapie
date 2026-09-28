export type AdminRole = 'ROLE_ADMIN';

export interface AdminUser {
  id: number;
  email: string;
  fullName: string;
  role: AdminRole;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  invitationCode: string;
}

export interface AuthResponse {
  tokenType: 'Bearer';
  accessToken: string;
  expiresInMs: number;
  user: AdminUser;
}
