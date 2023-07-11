import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRoomData, NewRoomData } from '../room-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoomData for edit and NewRoomDataFormGroupInput for create.
 */
type RoomDataFormGroupInput = IRoomData | PartialWithRequiredKeyOf<NewRoomData>;

type RoomDataFormDefaults = Pick<NewRoomData, 'id'>;

type RoomDataFormGroupContent = {
  id: FormControl<IRoomData['id'] | NewRoomData['id']>;
  name: FormControl<IRoomData['name']>;
  tier: FormControl<IRoomData['tier']>;
};

export type RoomDataFormGroup = FormGroup<RoomDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomDataFormService {
  createRoomDataFormGroup(roomData: RoomDataFormGroupInput = { id: null }): RoomDataFormGroup {
    const roomDataRawValue = {
      ...this.getFormDefaults(),
      ...roomData,
    };
    return new FormGroup<RoomDataFormGroupContent>({
      id: new FormControl(
        { value: roomDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(roomDataRawValue.name),
      tier: new FormControl(roomDataRawValue.tier),
    });
  }

  getRoomData(form: RoomDataFormGroup): IRoomData | NewRoomData {
    return form.getRawValue() as IRoomData | NewRoomData;
  }

  resetForm(form: RoomDataFormGroup, roomData: RoomDataFormGroupInput): void {
    const roomDataRawValue = { ...this.getFormDefaults(), ...roomData };
    form.reset(
      {
        ...roomDataRawValue,
        id: { value: roomDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RoomDataFormDefaults {
    return {
      id: null,
    };
  }
}
