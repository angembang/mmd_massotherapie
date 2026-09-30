import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { MassagePricesComponent } from './massage-prices.component';

describe('MassagePricesComponent', () => {
  let component: MassagePricesComponent;
  let fixture: ComponentFixture<MassagePricesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MassagePricesComponent],
      providers: [provideRouter([])]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MassagePricesComponent);
      fixture.componentRef.setInput('massages', [
                                       {
                                         id: 1,
                                         name: 'Massage cupping',
                                         slug: 'massage-cupping',
                                         icon: 'images/icons/icon_cupping.png',
                                         image: 'images/massage_price.jpg',
                                         options: [
                                           {
                                             id: 1,
                                             durationMinutes: 45,
                                             bodyArea: 'UPPER_OR_LOWER_BODY',
                                             priceCents: 6000
                                           },
                                           {
                                             id: 2,
                                             durationMinutes: 70,
                                             bodyArea: 'FULL_BODY',
                                             priceCents: 9000
                                           }
                                         ]
                                       }
                                     ]);

      fixture.detectChanges();

      component = fixture.componentInstance;
    });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
