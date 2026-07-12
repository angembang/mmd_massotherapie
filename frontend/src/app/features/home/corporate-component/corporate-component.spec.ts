import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorporateComponent } from './corporate-component';

describe('CorporateComponent', () => {
  let component: CorporateComponent;
  let fixture: ComponentFixture<CorporateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorporateComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CorporateComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
