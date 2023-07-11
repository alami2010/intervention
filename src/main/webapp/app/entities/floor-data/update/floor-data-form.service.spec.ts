import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../floor-data.test-samples';

import { FloorDataFormService } from './floor-data-form.service';

describe('FloorData Form Service', () => {
  let service: FloorDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FloorDataFormService);
  });

  describe('Service methods', () => {
    describe('createFloorDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFloorDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IFloorData should create a new form with FormGroup', () => {
        const formGroup = service.createFloorDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getFloorData', () => {
      it('should return NewFloorData for default FloorData initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFloorDataFormGroup(sampleWithNewData);

        const floorData = service.getFloorData(formGroup) as any;

        expect(floorData).toMatchObject(sampleWithNewData);
      });

      it('should return NewFloorData for empty FloorData initial value', () => {
        const formGroup = service.createFloorDataFormGroup();

        const floorData = service.getFloorData(formGroup) as any;

        expect(floorData).toMatchObject({});
      });

      it('should return IFloorData', () => {
        const formGroup = service.createFloorDataFormGroup(sampleWithRequiredData);

        const floorData = service.getFloorData(formGroup) as any;

        expect(floorData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFloorData should not enable id FormControl', () => {
        const formGroup = service.createFloorDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFloorData should disable id FormControl', () => {
        const formGroup = service.createFloorDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
