import { Component, input } from '@angular/core';
import { MassagePricesModel } from '../../../models/massage.prices.model';

@Component({
  selector: 'app-massage-prices-component',
  imports: [],
  templateUrl: './massage-prices-component.html',
  styleUrl: './massage-prices-component.scss',
})
export class MassagePricesComponent {
  massagePrices = input.required<MassagePricesModel[]>();
}
