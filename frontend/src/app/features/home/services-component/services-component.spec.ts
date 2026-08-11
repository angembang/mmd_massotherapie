import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServicesComponent } from './services-component';

describe('ServicesComponent', () => {
  let component: ServicesComponent;
  let fixture: ComponentFixture<ServicesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServicesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServicesComponent);
    fixture.componentRef.setInput('services', [
      {
        image: 'images/massage_price.jpg',
         point:'.',
         title: 'Massage cupping',
         firstText: 'Haut/bas du corps - 45 min : 70€'
      }
    ]);

    fixture.detectChanges();

    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
