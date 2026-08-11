import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MassagePriceComponent } from './massage-price-component';

describe('MassagePriceComponent', () => {
  let component: MassagePriceComponent;
  let fixture: ComponentFixture<MassagePriceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MassagePriceComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MassagePriceComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
