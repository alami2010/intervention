import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomDataFormService, RoomDataFormGroup } from './room-data-form.service';
import { IRoomData } from '../room-data.model';
import { RoomDataService } from '../service/room-data.service';
import { ITierData } from 'app/entities/tier-data/tier-data.model';
import { TierDataService } from 'app/entities/tier-data/service/tier-data.service';

@Component({
  selector: 'jhi-room-data-update',
  templateUrl: './room-data-update.component.html',
})
export class RoomDataUpdateComponent implements OnInit {
  isSaving = false;
  roomData: IRoomData | null = null;

  tierDataSharedCollection: ITierData[] = [];

  editForm: RoomDataFormGroup = this.roomDataFormService.createRoomDataFormGroup();

  constructor(
    protected roomDataService: RoomDataService,
    protected roomDataFormService: RoomDataFormService,
    protected tierDataService: TierDataService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTierData = (o1: ITierData | null, o2: ITierData | null): boolean => this.tierDataService.compareTierData(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomData }) => {
      this.roomData = roomData;
      if (roomData) {
        this.updateForm(roomData);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomData = this.roomDataFormService.getRoomData(this.editForm);
    if (roomData.id !== null) {
      this.subscribeToSaveResponse(this.roomDataService.update(roomData));
    } else {
      this.subscribeToSaveResponse(this.roomDataService.create(roomData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomData>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(roomData: IRoomData): void {
    this.roomData = roomData;
    this.roomDataFormService.resetForm(this.editForm, roomData);

    this.tierDataSharedCollection = this.tierDataService.addTierDataToCollectionIfMissing<ITierData>(
      this.tierDataSharedCollection,
      roomData.tier
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tierDataService
      .query()
      .pipe(map((res: HttpResponse<ITierData[]>) => res.body ?? []))
      .pipe(map((tierData: ITierData[]) => this.tierDataService.addTierDataToCollectionIfMissing<ITierData>(tierData, this.roomData?.tier)))
      .subscribe((tierData: ITierData[]) => (this.tierDataSharedCollection = tierData));
  }
}
