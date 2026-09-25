import { Component, inject } from '@angular/core';
import {ContactComponent} from '../contact/contact.component';
import {CorporateComponent} from '../corporate/corporate.component';
import {HeroComponent} from '../hero/hero.component';
import {ServicesComponent} from '../services/services.component';
import {MassagePricesComponent} from '../massage-prices/massage-prices.component';
import {FooterComponent} from '../../../layout/footer/footer.component';
import {HomeFacade} from '../../../facades/home.facade';

@Component({
  selector: 'app-home-component',
  imports: [
    ContactComponent,
    CorporateComponent,
    HeroComponent,
    ServicesComponent,
    MassagePricesComponent,
    FooterComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  readonly homeFacade = inject(HomeFacade);
}
