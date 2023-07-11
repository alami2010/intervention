import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FloorFormService, FloorFormGroup } from './floor-form.service';
import { IFloor } from '../floor.model';
import { FloorService } from '../service/floor.service';
import { IIntervention } from 'app/entities/intervention/intervention.model';
import { InterventionService } from 'app/entities/intervention/service/intervention.service';

@Component({
  selector: 'jhi-floor-update',
  templateUrl: './floor-update.component.html',
})
export class FloorUpdateComponent implements OnInit {
  isSaving = false;
  floor: IFloor | null = null;

  interventionsSharedCollection: IIntervention[] = [];

  editForm: FloorFormGroup = this.floorFormService.createFloorFormGroup();

  constructor(
    protected floorService: FloorService,
    protected floorFormService: FloorFormService,
    protected interventionService: InterventionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareIntervention = (o1: IIntervention | null, o2: IIntervention | null): boolean =>
    this.interventionService.compareIntervention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ floor }) => {
      this.floor = floor;
      if (floor) {
        this.updateForm(floor);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const floor = this.floorFormService.getFloor(this.editForm);
    if (floor.id !== null) {
      this.subscribeToSaveResponse(this.floorService.update(floor));
    } else {
      this.subscribeToSaveResponse(this.floorService.create(floor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFloor>>): void {
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

  protected updateForm(floor: IFloor): void {
    this.floor = floor;
    this.floorFormService.resetForm(this.editForm, floor);

    this.interventionsSharedCollection = this.interventionService.addInterventionToCollectionIfMissing<IIntervention>(
      this.interventionsSharedCollection,
      floor.intervention
    );
  }

  protected loadRelationshipsOptions(): void {
    this.interventionService
      .query()
      .pipe(map((res: HttpResponse<IIntervention[]>) => res.body ?? []))
      .pipe(
        map((interventions: IIntervention[]) =>
          this.interventionService.addInterventionToCollectionIfMissing<IIntervention>(interventions, this.floor?.intervention)
        )
      )
      .subscribe((interventions: IIntervention[]) => (this.interventionsSharedCollection = interventions));
  }
}
