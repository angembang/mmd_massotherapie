export interface Massage {
  id: number;
  name: string;
  slug: string;
  icon: string;
  image: string;
  options: MassageOption[];
}

export interface MassageOption {
  id: number;
  durationMinutes: number;
  bodyArea: BodyArea;
  priceCents: number;
}

export type BodyArea =
  | 'UPPER_OR_LOWER_BODY'
  | 'FULL_BODY'
  | 'NOT_APPLICABLE';
