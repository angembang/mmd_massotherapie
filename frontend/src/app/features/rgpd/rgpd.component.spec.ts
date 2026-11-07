import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RgpdComponent } from './rgpd.component';

describe('RgpdComponent', () => {
  let component: RgpdComponent;
  let fixture: ComponentFixture<RgpdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RgpdComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RgpdComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
