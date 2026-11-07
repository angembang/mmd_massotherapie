export interface Massage {
  id: number;
  name: string;
  slug: string;
  icon: string;
  image: string;
  options: MassageOption[];
}

export interface MassageDetail extends Massage {
  description: string;
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


/**
 * Represents a structured section of a massage description
 * Used to display Markdown-based content without HTML injection
 */
export interface MassageDescriptionSection {
  title: string | null;
  paragraphs: string[];
}
