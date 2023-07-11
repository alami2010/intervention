import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIntervention, NewIntervention } from '../intervention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIntervention for edit and NewInterventionFormGroupInput for create.
 */
type InterventionFormGroupInput = IIntervention | PartialWithRequiredKeyOf<NewIntervention>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIntervention | NewIntervention> = Omit<T, 'start' | 'finish'> & {
  start?: string | null;
  finish?: string | null;
};

type InterventionFormRawValue = FormValueOf<IIntervention>;

type NewInterventionFormRawValue = FormValueOf<NewIntervention>;

type InterventionFormDefaults = Pick<NewIntervention, 'id' | 'start' | 'finish'>;

type InterventionFormGroupContent = {
  id: FormControl<InterventionFormRawValue['id'] | NewIntervention['id']>;
  type: FormControl<InterventionFormRawValue['type']>;
  start: FormControl<InterventionFormRawValue['start']>;
  finish: FormControl<InterventionFormRawValue['finish']>;
  raison: FormControl<InterventionFormRawValue['raison']>;
  unitNumber: FormControl<InterventionFormRawValue['unitNumber']>;
  creationDate: FormControl<InterventionFormRawValue['creationDate']>;
  firstName: FormControl<string | null>;
};

export type InterventionFormGroup = FormGroup<InterventionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InterventionFormService {
  createInterventionFormGroup(intervention: InterventionFormGroupInput = { id: null }): InterventionFormGroup {
    const interventionRawValue = this.convertInterventionToInterventionRawValue({
      ...this.getFormDefaults(),
      ...intervention,
    });
    return new FormGroup<InterventionFormGroupContent>({
      id: new FormControl(
        { value: interventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      type: new FormControl(interventionRawValue.type),
      start: new FormControl(interventionRawValue.start),
      finish: new FormControl(interventionRawValue.finish),
      raison: new FormControl(interventionRawValue.raison),
      unitNumber: new FormControl(interventionRawValue.unitNumber),
      creationDate: new FormControl(interventionRawValue.creationDate),
      firstName: new FormControl(''),
    });
  }

  getIntervention(form: InterventionFormGroup): IIntervention | NewIntervention {
    return this.convertInterventionRawValueToIntervention(form.getRawValue() as InterventionFormRawValue | NewInterventionFormRawValue);
  }

  resetForm(form: InterventionFormGroup, intervention: InterventionFormGroupInput): void {
    const interventionRawValue = this.convertInterventionToInterventionRawValue({ ...this.getFormDefaults(), ...intervention });
    form.reset(
      {
        ...interventionRawValue,
        id: { value: interventionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InterventionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      start: currentTime,
      finish: currentTime,
    };
  }

  private convertInterventionRawValueToIntervention(
    rawIntervention: InterventionFormRawValue | NewInterventionFormRawValue
  ): IIntervention | NewIntervention {
    return {
      ...rawIntervention,
      start: dayjs(rawIntervention.start, DATE_TIME_FORMAT),
      finish: dayjs(rawIntervention.finish, DATE_TIME_FORMAT),
    };
  }

  private convertInterventionToInterventionRawValue(
    intervention: IIntervention | (Partial<NewIntervention> & InterventionFormDefaults)
  ): InterventionFormRawValue | PartialWithRequiredKeyOf<NewInterventionFormRawValue> {
    return {
      ...intervention,
      start: intervention.start ? intervention.start.format(DATE_TIME_FORMAT) : undefined,
      finish: intervention.finish ? intervention.finish.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
