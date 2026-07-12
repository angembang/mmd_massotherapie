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
