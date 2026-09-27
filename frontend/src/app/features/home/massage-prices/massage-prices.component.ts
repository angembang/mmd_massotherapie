import { Component, input } from '@angular/core';
import { Massage } from '../../../models/massage.model';

@Component({
  selector: 'app-massage-prices-component',
  imports: [],
  templateUrl: './massage-prices.component.html',
  styleUrl: './massage-prices.component.scss',
})
export class MassagePricesComponent {
  massages = input.required<Massage[]>();

    formatDuration(minutes: number): string {
      if (minutes < 60) {
        return `${minutes} min`;
      }

      const hours = Math.floor(minutes / 60);
      const remainingMinutes = minutes % 60;

      return remainingMinutes === 0
        ? `${hours}h`
        : `${hours}h${remainingMinutes}`;
    }

    formatBodyArea(bodyArea: string): string {
      switch (bodyArea) {
        case 'UPPER_OR_LOWER_BODY':
          return 'Haut/bas du corps';

        case 'FULL_BODY':
          return 'Corps complet';

        default:
          return '';
      }
    }
  }
