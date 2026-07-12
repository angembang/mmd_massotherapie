import {Component, input} from '@angular/core';
import {IconComponent} from '../../../shared/components/icon-component/icon-component';

@Component({
  selector: 'app-contact-component',
  imports: [
    IconComponent
  ],
  templateUrl: './contact-component.html',
  styleUrl: './contact-component.scss',
})
export class ContactComponent {
  phoneDisplay = input.required<string>();
  phoneLink = input.required<string>();
  instagramHandle = input.required<string>();
  instagramLink = input.required<string>();

}
