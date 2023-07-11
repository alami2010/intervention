import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RoomFormService, RoomFormGroup } from './room-form.service';
import { IRoom } from '../room.model';
import { RoomService } from '../service/room.service';
import { ITier } from 'app/entities/tier/tier.model';
import { TierService } from 'app/entities/tier/service/tier.service';

@Component({
  selector: 'jhi-room-update',
  templateUrl: './room-update.component.html',
})
export class RoomUpdateComponent implements OnInit {
  isSaving = false;
  room: IRoom | null = null;

  tiersSharedCollection: ITier[] = [];

  editForm: RoomFormGroup = this.roomFormService.createRoomFormGroup();

  constructor(
    protected roomService: RoomService,
    protected roomFormService: RoomFormService,
    protected tierService: TierService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTier = (o1: ITier | null, o2: ITier | null): boolean => this.tierService.compareTier(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ room }) => {
      this.room = room;
      if (room) {
        this.updateForm(room);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const room = this.roomFormService.getRoom(this.editForm);
    if (room.id !== null) {
      this.subscribeToSaveResponse(this.roomService.update(room));
    } else {
      this.subscribeToSaveResponse(this.roomService.create(room));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoom>>): void {
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

  protected updateForm(room: IRoom): void {
    this.room = room;
    this.roomFormService.resetForm(this.editForm, room);

    this.tiersSharedCollection = this.tierService.addTierToCollectionIfMissing<ITier>(this.tiersSharedCollection, room.tier);
  }

  protected loadRelationshipsOptions(): void {
    this.tierService
      .query()
      .pipe(map((res: HttpResponse<ITier[]>) => res.body ?? []))
      .pipe(map((tiers: ITier[]) => this.tierService.addTierToCollectionIfMissing<ITier>(tiers, this.room?.tier)))
      .subscribe((tiers: ITier[]) => (this.tiersSharedCollection = tiers));
  }
}
