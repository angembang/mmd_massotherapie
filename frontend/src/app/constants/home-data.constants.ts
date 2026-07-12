import {MassageServiceModel} from '../models/massage.service.model';
import {BenefitModel} from '../models/benefit.model';
import {ReasonModel} from '../models/reason.model';

export const SERVICES: MassageServiceModel[] = [
  {
    title: 'Préparation',
    description: 'Prépare le corps avant l\'effort.',
    icon: 'run'
  },
  {
    title: 'Relaxation musculaire',
    description: 'Détend, relâche les tensions.',
    icon: 'relax'
  },
  {
    title: 'Récupération',
    description: 'Accélère la récupération, réduit les courbatures.',
    icon: 'recover'
  },
  {
    title: 'Cupping thérapie',
    description: 'Améliore la circulation, soulage les douleurs musculaires.',
    icon: 'cupping'
  },
  {
    title: 'Amma assis',
    description: 'Massage rapide, énergisant et décontractant.',
    icon: 'seated'
  }
];

export const BENEFITS: BenefitModel[] = [
  { icon: 'stress', textBefore: 'Réduction du ', highlight: 'stress ', textAfter: 'et de la fatigue' },
  { icon: 'tension', textBefore: 'Soulage les ', highlight: 'tensions ', textAfter: ' (dos, nuque, épaules)' },
  { icon: 'focus', textBefore: 'Améliore la ', highlight: 'concentration ', textAfter: '& la ', highlight2: 'productivité' },
  { icon: 'circulation', textBefore: 'Stimule la ', highlight: 'circulation ', textAfter: 'sanguine' },
  { icon: 'energy', textBefore: 'Détente immédiate & regain ', highlight: 'd\'énergie' }
];

export const REASONS: ReasonModel[] = [
  { icon: 'heart', title: 'Bien-être', highlight: 'des salariés' },
  { icon: 'brain', title: 'Réduction du', highlight: 'stress & TMS' },
  { icon: 'growth', title: 'Motivation', highlight: '& QVT' },
  { icon: 'stars', title: 'Image positive', highlight: 'de l\'entreprise' }
];
