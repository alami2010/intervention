import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFloorData } from '../floor-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../floor-data.test-samples';

import { FloorDataService } from './floor-data.service';

const requireRestSample: IFloorData = {
  ...sampleWithRequiredData,
};

describe('FloorData Service', () => {
  let service: FloorDataService;
  let httpMock: HttpTestingController;
  let expectedResult: IFloorData | IFloorData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FloorDataService);
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

    it('should create a FloorData', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const floorData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(floorData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FloorData', () => {
      const floorData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(floorData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FloorData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FloorData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FloorData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFloorDataToCollectionIfMissing', () => {
      it('should add a FloorData to an empty array', () => {
        const floorData: IFloorData = sampleWithRequiredData;
        expectedResult = service.addFloorDataToCollectionIfMissing([], floorData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(floorData);
      });

      it('should not add a FloorData to an array that contains it', () => {
        const floorData: IFloorData = sampleWithRequiredData;
        const floorDataCollection: IFloorData[] = [
          {
            ...floorData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFloorDataToCollectionIfMissing(floorDataCollection, floorData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FloorData to an array that doesn't contain it", () => {
        const floorData: IFloorData = sampleWithRequiredData;
        const floorDataCollection: IFloorData[] = [sampleWithPartialData];
        expectedResult = service.addFloorDataToCollectionIfMissing(floorDataCollection, floorData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(floorData);
      });

      it('should add only unique FloorData to an array', () => {
        const floorDataArray: IFloorData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const floorDataCollection: IFloorData[] = [sampleWithRequiredData];
        expectedResult = service.addFloorDataToCollectionIfMissing(floorDataCollection, ...floorDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const floorData: IFloorData = sampleWithRequiredData;
        const floorData2: IFloorData = sampleWithPartialData;
        expectedResult = service.addFloorDataToCollectionIfMissing([], floorData, floorData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(floorData);
        expect(expectedResult).toContain(floorData2);
      });

      it('should accept null and undefined values', () => {
        const floorData: IFloorData = sampleWithRequiredData;
        expectedResult = service.addFloorDataToCollectionIfMissing([], null, floorData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(floorData);
      });

      it('should return initial array if no FloorData is added', () => {
        const floorDataCollection: IFloorData[] = [sampleWithRequiredData];
        expectedResult = service.addFloorDataToCollectionIfMissing(floorDataCollection, undefined, null);
        expect(expectedResult).toEqual(floorDataCollection);
      });
    });

    describe('compareFloorData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFloorData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFloorData(entity1, entity2);
        const compareResult2 = service.compareFloorData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFloorData(entity1, entity2);
        const compareResult2 = service.compareFloorData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFloorData(entity1, entity2);
        const compareResult2 = service.compareFloorData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
