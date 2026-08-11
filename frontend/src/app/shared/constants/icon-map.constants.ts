import { Type } from '@angular/core';

import {
  LucidePersonStanding,
  LucideSparkles,
  LucideCircleDot,
  LucideArmchair,
  LucideHeart,
  LucideBrain,
  LucideTrendingUp,
  LucideZap,
  LucideRefreshCw,
  LucideUserRound,
  LucideSun,
  LucidePhoneCall,
  LucideMapPin,
  LucideClock3
} from '@lucide/angular';

export const ICON_MAP: Record<string, Type<any>> = {
  // Services massage
  run: LucidePersonStanding,
  relax: LucideSparkles,
  recover: LucideRefreshCw,
  cupping: LucideCircleDot,
  seated: LucideArmchair,
  // Benefits
  stress: LucideSun,
  tension: LucideUserRound,
  focus: LucideBrain,
  circulation: LucideHeart,
  energy: LucideZap,
  // Reasons
  heart: LucideHeart,
  brain: LucideBrain,
  growth: LucideTrendingUp,
  stars: LucideSparkles,
  // Contact
  phone: LucidePhoneCall,
  location: LucideMapPin,

  clock: LucideClock3
} as const;


