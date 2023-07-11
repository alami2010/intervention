import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFloorData } from '../floor-data.model';

@Component({
  selector: 'jhi-floor-data-detail',
  templateUrl: './floor-data-detail.component.html',
})
export class FloorDataDetailComponent implements OnInit {
  floorData: IFloorData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ floorData }) => {
      this.floorData = floorData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
