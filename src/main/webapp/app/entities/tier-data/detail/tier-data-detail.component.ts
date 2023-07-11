import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITierData } from '../tier-data.model';

@Component({
  selector: 'jhi-tier-data-detail',
  templateUrl: './tier-data-detail.component.html',
})
export class TierDataDetailComponent implements OnInit {
  tierData: ITierData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tierData }) => {
      this.tierData = tierData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
