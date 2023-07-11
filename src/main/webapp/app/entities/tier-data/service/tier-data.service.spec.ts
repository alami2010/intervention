import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITierData } from '../tier-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tier-data.test-samples';

import { TierDataService } from './tier-data.service';

const requireRestSample: ITierData = {
  ...sampleWithRequiredData,
};

describe('TierData Service', () => {
  let service: TierDataService;
  let httpMock: HttpTestingController;
  let expectedResult: ITierData | ITierData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TierDataService);
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

    it('should create a TierData', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tierData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tierData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TierData', () => {
      const tierData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tierData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TierData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TierData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TierData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTierDataToCollectionIfMissing', () => {
      it('should add a TierData to an empty array', () => {
        const tierData: ITierData = sampleWithRequiredData;
        expectedResult = service.addTierDataToCollectionIfMissing([], tierData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tierData);
      });

      it('should not add a TierData to an array that contains it', () => {
        const tierData: ITierData = sampleWithRequiredData;
        const tierDataCollection: ITierData[] = [
          {
            ...tierData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTierDataToCollectionIfMissing(tierDataCollection, tierData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TierData to an array that doesn't contain it", () => {
        const tierData: ITierData = sampleWithRequiredData;
        const tierDataCollection: ITierData[] = [sampleWithPartialData];
        expectedResult = service.addTierDataToCollectionIfMissing(tierDataCollection, tierData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tierData);
      });

      it('should add only unique TierData to an array', () => {
        const tierDataArray: ITierData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tierDataCollection: ITierData[] = [sampleWithRequiredData];
        expectedResult = service.addTierDataToCollectionIfMissing(tierDataCollection, ...tierDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tierData: ITierData = sampleWithRequiredData;
        const tierData2: ITierData = sampleWithPartialData;
        expectedResult = service.addTierDataToCollectionIfMissing([], tierData, tierData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tierData);
        expect(expectedResult).toContain(tierData2);
      });

      it('should accept null and undefined values', () => {
        const tierData: ITierData = sampleWithRequiredData;
        expectedResult = service.addTierDataToCollectionIfMissing([], null, tierData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tierData);
      });

      it('should return initial array if no TierData is added', () => {
        const tierDataCollection: ITierData[] = [sampleWithRequiredData];
        expectedResult = service.addTierDataToCollectionIfMissing(tierDataCollection, undefined, null);
        expect(expectedResult).toEqual(tierDataCollection);
      });
    });

    describe('compareTierData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTierData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTierData(entity1, entity2);
        const compareResult2 = service.compareTierData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTierData(entity1, entity2);
        const compareResult2 = service.compareTierData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTierData(entity1, entity2);
        const compareResult2 = service.compareTierData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
