import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IIntervention } from '../intervention.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../intervention.test-samples';

import { InterventionService, RestIntervention } from './intervention.service';

const requireRestSample: RestIntervention = {
  ...sampleWithRequiredData,
  start: sampleWithRequiredData.start?.toJSON(),
  finish: sampleWithRequiredData.finish?.toJSON(),
  creationDate: sampleWithRequiredData.creationDate?.format(DATE_FORMAT),
};

describe('Intervention Service', () => {
  let service: InterventionService;
  let httpMock: HttpTestingController;
  let expectedResult: IIntervention | IIntervention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InterventionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Intervention', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const intervention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(intervention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Intervention', () => {
      const intervention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(intervention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Intervention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Intervention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Intervention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInterventionToCollectionIfMissing', () => {
      it('should add a Intervention to an empty array', () => {
        const intervention: IIntervention = sampleWithRequiredData;
        expectedResult = service.addInterventionToCollectionIfMissing([], intervention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intervention);
      });

      it('should not add a Intervention to an array that contains it', () => {
        const intervention: IIntervention = sampleWithRequiredData;
        const interventionCollection: IIntervention[] = [
          {
            ...intervention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInterventionToCollectionIfMissing(interventionCollection, intervention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Intervention to an array that doesn't contain it", () => {
        const intervention: IIntervention = sampleWithRequiredData;
        const interventionCollection: IIntervention[] = [sampleWithPartialData];
        expectedResult = service.addInterventionToCollectionIfMissing(interventionCollection, intervention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intervention);
      });

      it('should add only unique Intervention to an array', () => {
        const interventionArray: IIntervention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const interventionCollection: IIntervention[] = [sampleWithRequiredData];
        expectedResult = service.addInterventionToCollectionIfMissing(interventionCollection, ...interventionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const intervention: IIntervention = sampleWithRequiredData;
        const intervention2: IIntervention = sampleWithPartialData;
        expectedResult = service.addInterventionToCollectionIfMissing([], intervention, intervention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(intervention);
        expect(expectedResult).toContain(intervention2);
      });

      it('should accept null and undefined values', () => {
        const intervention: IIntervention = sampleWithRequiredData;
        expectedResult = service.addInterventionToCollectionIfMissing([], null, intervention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(intervention);
      });

      it('should return initial array if no Intervention is added', () => {
        const interventionCollection: IIntervention[] = [sampleWithRequiredData];
        expectedResult = service.addInterventionToCollectionIfMissing(interventionCollection, undefined, null);
        expect(expectedResult).toEqual(interventionCollection);
      });
    });

    describe('compareIntervention', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIntervention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIntervention(entity1, entity2);
        const compareResult2 = service.compareIntervention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIntervention(entity1, entity2);
        const compareResult2 = service.compareIntervention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIntervention(entity1, entity2);
        const compareResult2 = service.compareIntervention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
