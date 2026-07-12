import {Component, input} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";

@Component({
  selector: 'app-hero-component',
    imports: [
        NgOptimizedImage
    ],
  templateUrl: './hero-component.html',
  styleUrl: './hero-component.scss',
})
export class HeroComponent {
  smsLink = input.required<string>();
  mailtoLink = input.required<string>();

}
