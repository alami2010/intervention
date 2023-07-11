import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tier.test-samples';

import { TierFormService } from './tier-form.service';

describe('Tier Form Service', () => {
  let service: TierFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TierFormService);
  });

  describe('Service methods', () => {
    describe('createTierFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTierFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            floor: expect.any(Object),
          })
        );
      });

      it('passing ITier should create a new form with FormGroup', () => {
        const formGroup = service.createTierFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            floor: expect.any(Object),
          })
        );
      });
    });

    describe('getTier', () => {
      it('should return NewTier for default Tier initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTierFormGroup(sampleWithNewData);

        const tier = service.getTier(formGroup) as any;

        expect(tier).toMatchObject(sampleWithNewData);
      });

      it('should return NewTier for empty Tier initial value', () => {
        const formGroup = service.createTierFormGroup();

        const tier = service.getTier(formGroup) as any;

        expect(tier).toMatchObject({});
      });

      it('should return ITier', () => {
        const formGroup = service.createTierFormGroup(sampleWithRequiredData);

        const tier = service.getTier(formGroup) as any;

        expect(tier).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITier should not enable id FormControl', () => {
        const formGroup = service.createTierFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTier should disable id FormControl', () => {
        const formGroup = service.createTierFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
