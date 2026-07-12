import {Component, input} from '@angular/core';
import {ICON_MAP} from '../../constants/icon-map.constants';
import {NgComponentOutlet} from '@angular/common';

@Component({
  selector: 'app-icon-component',
  imports: [
    NgComponentOutlet
  ],
  templateUrl: './icon-component.html',
  styleUrl: './icon-component.scss',
})
export class IconComponent {
  readonly name = input.required<keyof typeof ICON_MAP>();
  protected readonly ICON_MAP = ICON_MAP;
}
