import {Component, input} from '@angular/core';
import {NgOptimizedImage} from "@angular/common";
import {BenefitModel} from '../../../models/benefit.model';
import {ReasonModel} from '../../../models/reason.model';
import {IconComponent} from '../../../shared/components/icon-component/icon-component';

@Component({
  selector: 'app-corporate-component',
    imports: [
        NgOptimizedImage,
        IconComponent
    ],
  templateUrl: './corporate-component.html',
  styleUrl: './corporate-component.scss',
})
export class CorporateComponent {
  benefits = input.required<BenefitModel[]>();
  reasons = input.required<ReasonModel[]>();

}
