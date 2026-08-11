import {Injectable} from '@angular/core';
import {BENEFITS, MASSAGE_PRICES, REASONS, SERVICES} from '../constants/home-data.constants';
import {BUSINESS} from '../constants/business.constants';

@Injectable({
  providedIn: 'root'
})
export class HomeFacade {

  readonly pageData = {
    services: SERVICES,
    massagePrices: MASSAGE_PRICES,
    benefits: BENEFITS,
    reasons: REASONS
  };

  readonly contactData = {
    phoneDisplay: BUSINESS.phoneDisplay,
    phoneLink: BUSINESS.phoneLink,

    instagramHandle: BUSINESS.instagramHandle,
    instagramLink: BUSINESS.instagramLink,

    email: BUSINESS.email
  };

  readonly smsLink =
    `sms:+33603667619?body=${encodeURIComponent(
      'Bonjour, je souhaite prendre rendez-vous pour une séance de massage.'
    )}`;

  readonly mailtoLink =
    `mailto:${BUSINESS.email}?subject=${encodeURIComponent(
      'Demande de rendez-vous - MMD Massothérapie'
    )}&body=${encodeURIComponent(
      'Bonjour,\n\nJe souhaite prendre rendez-vous pour une séance de massage.\n\nDisponibilités souhaitées :\nType de massage :\n\nMerci,'
    )}`;
}
