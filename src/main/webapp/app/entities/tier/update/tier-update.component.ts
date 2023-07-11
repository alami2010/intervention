import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { TierFormService, TierFormGroup } from './tier-form.service';
import { ITier } from '../tier.model';
import { TierService } from '../service/tier.service';
import { IFloor } from 'app/entities/floor/floor.model';
import { FloorService } from 'app/entities/floor/service/floor.service';

@Component({
  selector: 'jhi-tier-update',
  templateUrl: './tier-update.component.html',
})
export class TierUpdateComponent implements OnInit {
  isSaving = false;
  tier: ITier | null = null;

  floorsSharedCollection: IFloor[] = [];

  editForm: TierFormGroup = this.tierFormService.createTierFormGroup();

  constructor(
    protected tierService: TierService,
    protected tierFormService: TierFormService,
    protected floorService: FloorService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFloor = (o1: IFloor | null, o2: IFloor | null): boolean => this.floorService.compareFloor(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tier }) => {
      this.tier = tier;
      if (tier) {
        this.updateForm(tier);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tier = this.tierFormService.getTier(this.editForm);
    if (tier.id !== null) {
      this.subscribeToSaveResponse(this.tierService.update(tier));
    } else {
      this.subscribeToSaveResponse(this.tierService.create(tier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITier>>): void {
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

  protected updateForm(tier: ITier): void {
    this.tier = tier;
    this.tierFormService.resetForm(this.editForm, tier);

    this.floorsSharedCollection = this.floorService.addFloorToCollectionIfMissing<IFloor>(this.floorsSharedCollection, tier.floor);
  }

  protected loadRelationshipsOptions(): void {
    this.floorService
      .query()
      .pipe(map((res: HttpResponse<IFloor[]>) => res.body ?? []))
      .pipe(map((floors: IFloor[]) => this.floorService.addFloorToCollectionIfMissing<IFloor>(floors, this.tier?.floor)))
      .subscribe((floors: IFloor[]) => (this.floorsSharedCollection = floors));
  }
}
