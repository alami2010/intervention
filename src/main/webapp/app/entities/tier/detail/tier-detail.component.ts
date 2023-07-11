import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITier } from '../tier.model';

@Component({
  selector: 'jhi-tier-detail',
  templateUrl: './tier-detail.component.html',
})
export class TierDetailComponent implements OnInit {
  tier: ITier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tier }) => {
      this.tier = tier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
