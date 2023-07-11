import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tier-data.test-samples';

import { TierDataFormService } from './tier-data-form.service';

describe('TierData Form Service', () => {
  let service: TierDataFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TierDataFormService);
  });

  describe('Service methods', () => {
    describe('createTierDataFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTierDataFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            floor: expect.any(Object),
          })
        );
      });

      it('passing ITierData should create a new form with FormGroup', () => {
        const formGroup = service.createTierDataFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            floor: expect.any(Object),
          })
        );
      });
    });

    describe('getTierData', () => {
      it('should return NewTierData for default TierData initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTierDataFormGroup(sampleWithNewData);

        const tierData = service.getTierData(formGroup) as any;

        expect(tierData).toMatchObject(sampleWithNewData);
      });

      it('should return NewTierData for empty TierData initial value', () => {
        const formGroup = service.createTierDataFormGroup();

        const tierData = service.getTierData(formGroup) as any;

        expect(tierData).toMatchObject({});
      });

      it('should return ITierData', () => {
        const formGroup = service.createTierDataFormGroup(sampleWithRequiredData);

        const tierData = service.getTierData(formGroup) as any;

        expect(tierData).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITierData should not enable id FormControl', () => {
        const formGroup = service.createTierDataFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTierData should disable id FormControl', () => {
        const formGroup = service.createTierDataFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
