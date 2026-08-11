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
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
