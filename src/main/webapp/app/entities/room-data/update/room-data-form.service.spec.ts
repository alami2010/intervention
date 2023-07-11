import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../room-data.test-samples';

import { RoomDataFormService } from './room-data-form.service';

describe('RoomData Form Service', () => {
  let service: RoomDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoomDataFormService);
  });

  describe('Service methods', () => {
    describe('createRoomDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRoomDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            tier: expect.any(Object),
          })
        );
      });

      it('passing IRoomData should create a new form with FormGroup', () => {
        const formGroup = service.createRoomDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            tier: expect.any(Object),
          })
        );
      });
    });

    describe('getRoomData', () => {
      it('should return NewRoomData for default RoomData initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createRoomDataFormGroup(sampleWithNewData);

        const roomData = service.getRoomData(formGroup) as any;

        expect(roomData).toMatchObject(sampleWithNewData);
      });

      it('should return NewRoomData for empty RoomData initial value', () => {
        const formGroup = service.createRoomDataFormGroup();

        const roomData = service.getRoomData(formGroup) as any;

        expect(roomData).toMatchObject({});
      });

      it('should return IRoomData', () => {
        const formGroup = service.createRoomDataFormGroup(sampleWithRequiredData);

        const roomData = service.getRoomData(formGroup) as any;

        expect(roomData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRoomData should not enable id FormControl', () => {
        const formGroup = service.createRoomDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRoomData should disable id FormControl', () => {
        const formGroup = service.createRoomDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
