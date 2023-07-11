import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoomData } from '../room-data.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../room-data.test-samples';

import { RoomDataService } from './room-data.service';

const requireRestSample: IRoomData = {
  ...sampleWithRequiredData,
};

describe('RoomData Service', () => {
  let service: RoomDataService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoomData | IRoomData[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoomDataService);
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

    it('should create a RoomData', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const roomData = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(roomData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoomData', () => {
      const roomData = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(roomData).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RoomData', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoomData', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RoomData', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRoomDataToCollectionIfMissing', () => {
      it('should add a RoomData to an empty array', () => {
        const roomData: IRoomData = sampleWithRequiredData;
        expectedResult = service.addRoomDataToCollectionIfMissing([], roomData);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomData);
      });

      it('should not add a RoomData to an array that contains it', () => {
        const roomData: IRoomData = sampleWithRequiredData;
        const roomDataCollection: IRoomData[] = [
          {
            ...roomData,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoomDataToCollectionIfMissing(roomDataCollection, roomData);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoomData to an array that doesn't contain it", () => {
        const roomData: IRoomData = sampleWithRequiredData;
        const roomDataCollection: IRoomData[] = [sampleWithPartialData];
        expectedResult = service.addRoomDataToCollectionIfMissing(roomDataCollection, roomData);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomData);
      });

      it('should add only unique RoomData to an array', () => {
        const roomDataArray: IRoomData[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roomDataCollection: IRoomData[] = [sampleWithRequiredData];
        expectedResult = service.addRoomDataToCollectionIfMissing(roomDataCollection, ...roomDataArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roomData: IRoomData = sampleWithRequiredData;
        const roomData2: IRoomData = sampleWithPartialData;
        expectedResult = service.addRoomDataToCollectionIfMissing([], roomData, roomData2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomData);
        expect(expectedResult).toContain(roomData2);
      });

      it('should accept null and undefined values', () => {
        const roomData: IRoomData = sampleWithRequiredData;
        expectedResult = service.addRoomDataToCollectionIfMissing([], null, roomData, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomData);
      });

      it('should return initial array if no RoomData is added', () => {
        const roomDataCollection: IRoomData[] = [sampleWithRequiredData];
        expectedResult = service.addRoomDataToCollectionIfMissing(roomDataCollection, undefined, null);
        expect(expectedResult).toEqual(roomDataCollection);
      });
    });

    describe('compareRoomData', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoomData(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRoomData(entity1, entity2);
        const compareResult2 = service.compareRoomData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRoomData(entity1, entity2);
        const compareResult2 = service.compareRoomData(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRoomData(entity1, entity2);
        const compareResult2 = service.compareRoomData(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
