import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFloor, NewFloor } from '../floor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFloor for edit and NewFloorFormGroupInput for create.
 */
type FloorFormGroupInput = IFloor | PartialWithRequiredKeyOf<NewFloor>;

type FloorFormDefaults = Pick<NewFloor, 'id'>;

type FloorFormGroupContent = {
  id: FormControl<IFloor['id'] | NewFloor['id']>;
  name: FormControl<IFloor['name']>;
  intervention: FormControl<IFloor['intervention']>;
};

export type FloorFormGroup = FormGroup<FloorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FloorFormService {
  createFloorFormGroup(floor: FloorFormGroupInput = { id: null }): FloorFormGroup {
    const floorRawValue = {
      ...this.getFormDefaults(),
      ...floor,
    };
    return new FormGroup<FloorFormGroupContent>({
      id: new FormControl(
        { value: floorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(floorRawValue.name),
      intervention: new FormControl(floorRawValue.intervention),
    });
  }

  getFloor(form: FloorFormGroup): IFloor | NewFloor {
    return form.getRawValue() as IFloor | NewFloor;
  }

  resetForm(form: FloorFormGroup, floor: FloorFormGroupInput): void {
    const floorRawValue = { ...this.getFormDefaults(), ...floor };
    form.reset(
      {
        ...floorRawValue,
        id: { value: floorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FloorFormDefaults {
    return {
      id: null,
    };
  }
}
