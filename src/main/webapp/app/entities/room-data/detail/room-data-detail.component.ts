import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoomData } from '../room-data.model';

@Component({
  selector: 'jhi-room-data-detail',
  templateUrl: './room-data-detail.component.html',
})
export class RoomDataDetailComponent implements OnInit {
  roomData: IRoomData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomData }) => {
      this.roomData = roomData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
