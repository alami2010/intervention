import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TierDataFormService, TierDataFormGroup } from './tier-data-form.service';
import { ITierData } from '../tier-data.model';
import { TierDataService } from '../service/tier-data.service';
import { IFloorData } from 'app/entities/floor-data/floor-data.model';
import { FloorDataService } from 'app/entities/floor-data/service/floor-data.service';

@Component({
  selector: 'jhi-tier-data-update',
  templateUrl: './tier-data-update.component.html',
})
export class TierDataUpdateComponent implements OnInit {
  isSaving = false;
  tierData: ITierData | null = null;

  floorDataSharedCollection: IFloorData[] = [];

  editForm: TierDataFormGroup = this.tierDataFormService.createTierDataFormGroup();

  constructor(
    protected tierDataService: TierDataService,
    protected tierDataFormService: TierDataFormService,
    protected floorDataService: FloorDataService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFloorData = (o1: IFloorData | null, o2: IFloorData | null): boolean => this.floorDataService.compareFloorData(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tierData }) => {
      this.tierData = tierData;
      if (tierData) {
        this.updateForm(tierData);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tierData = this.tierDataFormService.getTierData(this.editForm);
    if (tierData.id !== null) {
      this.subscribeToSaveResponse(this.tierDataService.update(tierData));
    } else {
      this.subscribeToSaveResponse(this.tierDataService.create(tierData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITierData>>): void {
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

  protected updateForm(tierData: ITierData): void {
    this.tierData = tierData;
    this.tierDataFormService.resetForm(this.editForm, tierData);

    this.floorDataSharedCollection = this.floorDataService.addFloorDataToCollectionIfMissing<IFloorData>(
      this.floorDataSharedCollection,
      tierData.floor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.floorDataService
      .query()
      .pipe(map((res: HttpResponse<IFloorData[]>) => res.body ?? []))
      .pipe(
        map((floorData: IFloorData[]) =>
          this.floorDataService.addFloorDataToCollectionIfMissing<IFloorData>(floorData, this.tierData?.floor)
        )
      )
      .subscribe((floorData: IFloorData[]) => (this.floorDataSharedCollection = floorData));
  }
}
