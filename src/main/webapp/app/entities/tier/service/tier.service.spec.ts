import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITier } from '../tier.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tier.test-samples';

import { TierService } from './tier.service';

const requireRestSample: ITier = {
  ...sampleWithRequiredData,
};

describe('Tier Service', () => {
  let service: TierService;
  let httpMock: HttpTestingController;
  let expectedResult: ITier | ITier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TierService);
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

    it('should create a Tier', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tier = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tier).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tier', () => {
      const tier = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tier).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tier', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tier', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tier', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTierToCollectionIfMissing', () => {
      it('should add a Tier to an empty array', () => {
        const tier: ITier = sampleWithRequiredData;
        expectedResult = service.addTierToCollectionIfMissing([], tier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tier);
      });

      it('should not add a Tier to an array that contains it', () => {
        const tier: ITier = sampleWithRequiredData;
        const tierCollection: ITier[] = [
          {
            ...tier,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTierToCollectionIfMissing(tierCollection, tier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tier to an array that doesn't contain it", () => {
        const tier: ITier = sampleWithRequiredData;
        const tierCollection: ITier[] = [sampleWithPartialData];
        expectedResult = service.addTierToCollectionIfMissing(tierCollection, tier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tier);
      });

      it('should add only unique Tier to an array', () => {
        const tierArray: ITier[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tierCollection: ITier[] = [sampleWithRequiredData];
        expectedResult = service.addTierToCollectionIfMissing(tierCollection, ...tierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tier: ITier = sampleWithRequiredData;
        const tier2: ITier = sampleWithPartialData;
        expectedResult = service.addTierToCollectionIfMissing([], tier, tier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tier);
        expect(expectedResult).toContain(tier2);
      });

      it('should accept null and undefined values', () => {
        const tier: ITier = sampleWithRequiredData;
        expectedResult = service.addTierToCollectionIfMissing([], null, tier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tier);
      });

      it('should return initial array if no Tier is added', () => {
        const tierCollection: ITier[] = [sampleWithRequiredData];
        expectedResult = service.addTierToCollectionIfMissing(tierCollection, undefined, null);
        expect(expectedResult).toEqual(tierCollection);
      });
    });

    describe('compareTier', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTier(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTier(entity1, entity2);
        const compareResult2 = service.compareTier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTier(entity1, entity2);
        const compareResult2 = service.compareTier(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTier(entity1, entity2);
        const compareResult2 = service.compareTier(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
