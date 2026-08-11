import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MassagePricesComponent } from './massage-prices-component';

describe('MassagePricesComponent', () => {
  let component: MassagePricesComponent;
  let fixture: ComponentFixture<MassagePricesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MassagePricesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MassagePricesComponent);
      fixture.componentRef.setInput('massagePrices', [
        {
          title: 'Préparation',
          description: 'Prépare le corps avant l\'effort.',
          icon: 'run'
        }
      ]);

      fixture.detectChanges();

      component = fixture.componentInstance;
    });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
