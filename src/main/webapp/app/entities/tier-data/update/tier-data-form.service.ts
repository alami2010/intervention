import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITierData, NewTierData } from '../tier-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITierData for edit and NewTierDataFormGroupInput for create.
 */
type TierDataFormGroupInput = ITierData | PartialWithRequiredKeyOf<NewTierData>;

type TierDataFormDefaults = Pick<NewTierData, 'id'>;

type TierDataFormGroupContent = {
  id: FormControl<ITierData['id'] | NewTierData['id']>;
  name: FormControl<ITierData['name']>;
  floor: FormControl<ITierData['floor']>;
};

export type TierDataFormGroup = FormGroup<TierDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TierDataFormService {
  createTierDataFormGroup(tierData: TierDataFormGroupInput = { id: null }): TierDataFormGroup {
    const tierDataRawValue = {
      ...this.getFormDefaults(),
      ...tierData,
    };
    return new FormGroup<TierDataFormGroupContent>({
      id: new FormControl(
        { value: tierDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(tierDataRawValue.name),
      floor: new FormControl(tierDataRawValue.floor),
    });
  }

  getTierData(form: TierDataFormGroup): ITierData | NewTierData {
    return form.getRawValue() as ITierData | NewTierData;
  }

  resetForm(form: TierDataFormGroup, tierData: TierDataFormGroupInput): void {
    const tierDataRawValue = { ...this.getFormDefaults(), ...tierData };
    form.reset(
      {
        ...tierDataRawValue,
        id: { value: tierDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TierDataFormDefaults {
    return {
      id: null,
    };
  }
}
