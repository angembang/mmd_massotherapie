import { Component, input, ElementRef, effect, signal, viewChild, DestroyRef, inject,  computed, } from '@angular/core';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { Subscription } from 'rxjs';
import { MassageService } from '../../../services/massage.service';
import { Massage, MassageDetail, MassageDescriptionSection } from '../../../models/massage.model';

@Component({
  selector: 'app-massage-prices-component',
  imports: [RouterLink],
  templateUrl: './massage-prices.component.html',
  styleUrl: './massage-prices.component.scss',
})
export class MassagePricesComponent {
  readonly massages = input.required<Massage[]>();

  readonly selectedMassage = signal<MassageDetail | null>(null);

  readonly descriptionSections = computed(() => {
    const description = this.selectedMassage()?.description ?? '';
    return this.parseDescription(description);
  });

  readonly detailsLoading = signal(false);
  readonly detailsError = signal<string | null>(null);

  private readonly massageService = inject(MassageService);
  private readonly destroyRef = inject(DestroyRef);

  private detailsSubscription?: Subscription;

  private readonly detailsDialog =
    viewChild<ElementRef<HTMLDialogElement>>('detailsDialog');

  constructor() {
    effect(() => {
      const dialog = this.detailsDialog()?.nativeElement;
      const massage = this.selectedMassage();

      if (!dialog) {
        return;
      }

      if (massage && !dialog.open) {
        dialog.showModal();
      } else if (!massage && dialog.open) {
        dialog.close();
      }
    });
  }

  openDetails(massage: Massage): void {
    // Cancels any previous request still in progress
    this.detailsSubscription?.unsubscribe();

    this.detailsError.set(null);
    this.detailsLoading.set(true);

    this.detailsSubscription = this.massageService
      .getMassageBySlug(massage.slug)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (details) => {
          this.detailsLoading.set(false);
          this.selectedMassage.set(details);
        },
        error: () => {
          this.detailsLoading.set(false);
          this.detailsError.set(
            'Impossible de charger les détails du massage.'
          );
        },
      }
    );
  }

  closeDetails(): void {
    this.selectedMassage.set(null);
  }

  onDialogClose(): void {
    this.selectedMassage.set(null);
  }

  formatDuration(minutes: number): string {
    if (minutes < 60) {
      return `${minutes} min`;
    }

    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;

    return remainingMinutes === 0
      ? `${hours}h`
      : `${hours}h${remainingMinutes}`;
  }

  formatBodyArea(bodyArea: string): string {
    switch (bodyArea) {
      case 'UPPER_OR_LOWER_BODY':
        return 'Haut/bas du corps';

      case 'FULL_BODY':
        return 'Corps complet';

      default:
        return '';
    }
  }

  private parseDescription(description: string): MassageDescriptionSection[] {
    const sections: MassageDescriptionSection[] = [];

    const normalized = description.replace(/\r\n?/g, '\n');

    // Each section begins with a Markdown heading (##).
    const blocks = normalized.split(/(?=^##\s+)/m);

    for (const block of blocks) {
      const content = block.trim();

      if (!content) {
        continue;
      }

      const lines = content.split('\n');
      const isHeading = /^##\s+/.test(lines[0]);

      const title = isHeading
        ? lines.shift()!.replace(/^##\s+/, '').trim()
        : null;

      const paragraphs = lines
        .join('\n')
        .split(/\n\s*\n/)
        .map(paragraph => paragraph.trim())
        .filter(Boolean);

      if (title || paragraphs.length > 0) {
        sections.push({ title, paragraphs });
      }
    }

    return sections;
  }
}
