import {Component, input} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {MassageServiceModel} from '../../../models/massage.service.model';
import {IconComponent} from '../../../shared/components/icon-component/icon-component';

@Component({
  selector: 'app-services-component',
    imports: [
        NgOptimizedImage,
        IconComponent
    ],
  templateUrl: './services-component.html',
  styleUrl: './services-component.scss',
})
export class ServicesComponent {
  services = input.required<MassageServiceModel[]>();
}
