import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITier, NewTier } from '../tier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITier for edit and NewTierFormGroupInput for create.
 */
type TierFormGroupInput = ITier | PartialWithRequiredKeyOf<NewTier>;

type TierFormDefaults = Pick<NewTier, 'id'>;

type TierFormGroupContent = {
  id: FormControl<ITier['id'] | NewTier['id']>;
  name: FormControl<ITier['name']>;
  floor: FormControl<ITier['floor']>;
};

export type TierFormGroup = FormGroup<TierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TierFormService {
  createTierFormGroup(tier: TierFormGroupInput = { id: null }): TierFormGroup {
    const tierRawValue = {
      ...this.getFormDefaults(),
      ...tier,
    };
    return new FormGroup<TierFormGroupContent>({
      id: new FormControl(
        { value: tierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(tierRawValue.name),
      floor: new FormControl(tierRawValue.floor),
    });
  }

  getTier(form: TierFormGroup): ITier | NewTier {
    return form.getRawValue() as ITier | NewTier;
  }

  resetForm(form: TierFormGroup, tier: TierFormGroupInput): void {
    const tierRawValue = { ...this.getFormDefaults(), ...tier };
    form.reset(
      {
        ...tierRawValue,
        id: { value: tierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TierFormDefaults {
    return {
      id: null,
    };
  }
}
