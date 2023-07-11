import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { InterventionFormGroup, InterventionFormService } from './intervention-form.service';
import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';
import { TypeIntervention } from 'app/entities/enumerations/type-intervention.model';
import { IFloor } from '../../floor/floor.model';
import { ITier } from '../../tier/tier.model';
import { IRoom } from '../../room/room.model';

function getT(name: any) {
  return { name: name, checked: false } as ITier;
}

function getR(name: any) {
  return { name: name, checked: false } as IRoom;
}

function getTiersByFloor(floor: any): ITier[] {
  switch (floor) {
    case '7': {
      return [getT('01'), getT('06'), getT('07')];
    }
    case '8--15': {
      return [getT('01'), getT('02'), getT('03'), getT('04'), getT('05'), getT('06'), getT('07')];
    }

    case '16--18': {
      return [getT('01'), getT('02'), getT('03'), getT('04'), getT('05'), getT('06')];
    }

    case '19--26': {
      return [getT('01'), getT('02'), getT('03'), getT('04'), getT('05'), getT('06')];
    }

    case '27--30': {
      return [getT('01'), getT('02'), getT('03'), getT('04'), getT('05')];
    }

    case '31--42': {
      return [getT('01'), getT('02'), getT('03'), getT('04'), getT('05')];
    }

    case '43--48': {
      return [getT('01'), getT('02')];
    }
  }

  return [];
}

function getRoomByFloor(floor: any): IRoom[] {
  switch (floor) {
    case '7': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Kitchen'), getR('Laundry')];
    }
    case '8--15': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }

    case '16--18': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }

    case '19--26': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }

    case '27--30': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }

    case '31--42': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }

    case '43--48': {
      return [getR('Primary Bathroom'), getR('Bathroom 2'), getR('Bathroom 3'), getR('Powder room'), getR('Kitchen'), getR('Laundry')];
    }
  }

  return [];
}

@Component({
  selector: 'jhi-intervention-update',
  templateUrl: './intervention-update.component.html',
})
export class InterventionUpdateComponent implements OnInit {
  isSaving = false;
  intervention: IIntervention = { id: -1, floors: [] };
  typeInterventionValues = Object.keys(TypeIntervention);

  editForm: InterventionFormGroup = this.interventionFormService.createInterventionFormGroup();
  floorsCollection: string[] = ['7', '8--15', '16--18', '19--26', '27--30', '31--42', '43--48'];

  constructor(
    protected interventionService: InterventionService,
    protected interventionFormService: InterventionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      if (intervention) {
        this.intervention = intervention;
        this.updateForm(intervention);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const intervention = this.intervention;
    console.info(this.intervention);
    if (intervention.id !== null && intervention.id != -1) {
      this.subscribeToSaveResponse(this.interventionService.update(intervention));
    } else {
      this.subscribeToSaveResponse(this.interventionService.create(intervention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIntervention>>): void {
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

  protected updateForm(intervention: IIntervention): void {
    this.intervention = intervention;
    this.interventionFormService.resetForm(this.editForm, intervention);
  }

  addFloor() {
    console.info('dddd');
    console.info(this.intervention);
    console.info(this.intervention?.floors);

    if (!this.intervention?.floors) {
      // @ts-ignore
      this.intervention.floors = [];
    }
    // @ts-ignore
    this.intervention?.floors?.push({} as IFloor);

    console.info(this.intervention);
  }

  onChange(value: any, floor: IFloor) {
    console.log(value);

    floor.tiers = getTiersByFloor(value);

    floor.tiers.forEach(value1 => {
      value1.rooms = getRoomByFloor(value);
    });
  }
}
