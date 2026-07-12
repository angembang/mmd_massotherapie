import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroComponent } from './hero-component';

describe('HeroComponent', () => {
  let component: HeroComponent;
  let fixture: ComponentFixture<HeroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeroComponent);
    fixture.componentRef.setInput(
      'smsLink',
      'sms:+33603667619'
    );

    fixture.componentRef.setInput(
      'mailtoLink',
      'mailto:test@test.com'
    );

    fixture.detectChanges();

    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
