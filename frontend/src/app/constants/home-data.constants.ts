import { MassageServiceModel } from '../models/massage.service.model';
import { BenefitModel } from '../models/benefit.model';
import { ReasonModel } from '../models/reason.model';
import { MassagePricesModel } from "../models/massage.prices.model";

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

export const MASSAGE_PRICES: MassagePricesModel[] = [
  { image: 'images/icons/icon_cupping.png', title: 'MASSAGE CUPPING', point: '•', firstText: 'Haut/bas du corps - 45 min : 60€', lastText: 'Corps complet - 1h10 : 90€' },
  { image: 'images/icons/icon_muscle.png', title: 'MASSAGE SPORTIF + VENTOUSES CHAUDES', point: '•', firstText: 'Haut/bas du corps - 45 min : 60€', lastText: 'Corps complet - 1h10 : 90€' },
  { image: 'images/icons/icon_drainage.png', title: 'DRAINAGE LYMPHATIQUE', point: '•', firstText: 'Corps complet : 80€' },
  { image: 'images/icons/icon_therapeutique.png', title: 'MASSAGE THÉRAPEUTIQUE', point: '•', firstText: '45 min : 60€' },
  { image: 'images/icons/icon_seated.png', title: 'MASSAGE AMMA ASSIS', point: '•', firstText: '20 à 30 min : 30€' },
  { image: 'images/icons/icon_pressotherapie.png', title: 'BOTTES PRESSOTHÉRAPIE', point: '•', firstText: 'Une séance de 45 min : 30€' }
];


