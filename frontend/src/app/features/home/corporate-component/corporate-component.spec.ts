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
    fixture.componentRef.setInput('benefits', [
      {
        icon: 'stress',
        textBefore: 'Réduction du ',
        highlight: 'stress',
        textAfter: ' et de la fatigue'
      }
    ]);

    fixture.componentRef.setInput('reasons', [
      {
        icon: 'heart',
        title: 'Bien-être',
        highlight: 'des salariés'
      }
    ]);

    fixture.detectChanges();

    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
