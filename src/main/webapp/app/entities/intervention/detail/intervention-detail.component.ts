import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntervention } from '../intervention.model';

@Component({
  selector: 'jhi-intervention-detail',
  templateUrl: './intervention-detail.component.html',
})
export class InterventionDetailComponent implements OnInit {
  intervention: IIntervention | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      this.intervention = intervention;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
