import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactComponent } from './contact-component';

describe('ContactComponent', () => {
  let component: ContactComponent;
  let fixture: ComponentFixture<ContactComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContactComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContactComponent);
    fixture.componentRef.setInput('phoneDisplay', '06 03 66 76 19');
    fixture.componentRef.setInput('phoneLink', 'tel:+33603667619');
    fixture.componentRef.setInput('instagramHandle', 'mmd.massotherapeute');
    fixture.componentRef.setInput('instagramLink', 'https://instagram.com/mmd.massotherapeute');

    fixture.detectChanges();

    component = fixture.componentInstance;
  });
  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
