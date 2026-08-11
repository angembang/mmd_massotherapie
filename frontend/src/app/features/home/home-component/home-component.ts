import { Component, inject } from '@angular/core';
import {ContactComponent} from '../contact-component/contact-component';
import {CorporateComponent} from '../corporate-component/corporate-component';
import {HeroComponent} from '../hero-component/hero-component';
import {ServicesComponent} from '../services-component/services-component';
import {MassagePricesComponent} from '../massage-prices-component/massage-prices-component';
import {FooterComponent} from '../footer-component/footer-component';
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
  templateUrl: './home-component.html',
  styleUrl: './home-component.scss',
})
export class HomeComponent {
  readonly homeFacade = inject(HomeFacade);
}
