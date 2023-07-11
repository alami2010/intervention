import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../intervention.test-samples';

import { InterventionFormService } from './intervention-form.service';

describe('Intervention Form Service', () => {
  let service: InterventionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InterventionFormService);
  });

  describe('Service methods', () => {
    describe('createInterventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInterventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            start: expect.any(Object),
            finish: expect.any(Object),
            raison: expect.any(Object),
            unitNumber: expect.any(Object),
            creationDate: expect.any(Object),
          })
        );
      });

      it('passing IIntervention should create a new form with FormGroup', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            start: expect.any(Object),
            finish: expect.any(Object),
            raison: expect.any(Object),
            unitNumber: expect.any(Object),
            creationDate: expect.any(Object),
          })
        );
      });
    });

    describe('getIntervention', () => {
      it('should return NewIntervention for default Intervention initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInterventionFormGroup(sampleWithNewData);

        const intervention = service.getIntervention(formGroup) as any;

        expect(intervention).toMatchObject(sampleWithNewData);
      });

      it('should return NewIntervention for empty Intervention initial value', () => {
        const formGroup = service.createInterventionFormGroup();

        const intervention = service.getIntervention(formGroup) as any;

        expect(intervention).toMatchObject({});
      });

      it('should return IIntervention', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);

        const intervention = service.getIntervention(formGroup) as any;

        expect(intervention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIntervention should not enable id FormControl', () => {
        const formGroup = service.createInterventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIntervention should disable id FormControl', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
