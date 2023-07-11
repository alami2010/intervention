import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFloorData, NewFloorData } from '../floor-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFloorData for edit and NewFloorDataFormGroupInput for create.
 */
type FloorDataFormGroupInput = IFloorData | PartialWithRequiredKeyOf<NewFloorData>;

type FloorDataFormDefaults = Pick<NewFloorData, 'id'>;

type FloorDataFormGroupContent = {
  id: FormControl<IFloorData['id'] | NewFloorData['id']>;
  name: FormControl<IFloorData['name']>;
};

export type FloorDataFormGroup = FormGroup<FloorDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FloorDataFormService {
  createFloorDataFormGroup(floorData: FloorDataFormGroupInput = { id: null }): FloorDataFormGroup {
    const floorDataRawValue = {
      ...this.getFormDefaults(),
      ...floorData,
    };
    return new FormGroup<FloorDataFormGroupContent>({
      id: new FormControl(
        { value: floorDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(floorDataRawValue.name),
    });
  }

  getFloorData(form: FloorDataFormGroup): IFloorData | NewFloorData {
    return form.getRawValue() as IFloorData | NewFloorData;
  }

  resetForm(form: FloorDataFormGroup, floorData: FloorDataFormGroupInput): void {
    const floorDataRawValue = { ...this.getFormDefaults(), ...floorData };
    form.reset(
      {
        ...floorDataRawValue,
        id: { value: floorDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FloorDataFormDefaults {
    return {
      id: null,
    };
  }
}
