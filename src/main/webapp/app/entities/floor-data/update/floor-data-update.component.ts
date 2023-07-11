import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FloorDataFormService, FloorDataFormGroup } from './floor-data-form.service';
import { IFloorData } from '../floor-data.model';
import { FloorDataService } from '../service/floor-data.service';

@Component({
  selector: 'jhi-floor-data-update',
  templateUrl: './floor-data-update.component.html',
})
export class FloorDataUpdateComponent implements OnInit {
  isSaving = false;
  floorData: IFloorData | null = null;

  editForm: FloorDataFormGroup = this.floorDataFormService.createFloorDataFormGroup();

  constructor(
    protected floorDataService: FloorDataService,
    protected floorDataFormService: FloorDataFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ floorData }) => {
      this.floorData = floorData;
      if (floorData) {
        this.updateForm(floorData);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const floorData = this.floorDataFormService.getFloorData(this.editForm);
    if (floorData.id !== null) {
      this.subscribeToSaveResponse(this.floorDataService.update(floorData));
    } else {
      this.subscribeToSaveResponse(this.floorDataService.create(floorData));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFloorData>>): void {
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

  protected updateForm(floorData: IFloorData): void {
    this.floorData = floorData;
    this.floorDataFormService.resetForm(this.editForm, floorData);
  }
}
