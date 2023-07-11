import { IRoomData, NewRoomData } from './room-data.model';

export const sampleWithRequiredData: IRoomData = {
  id: 30051,
};

export const sampleWithPartialData: IRoomData = {
  id: 41672,
  name: 'Games Jersey',
};

export const sampleWithFullData: IRoomData = {
  id: 18074,
  name: 'whiteboard',
};

export const sampleWithNewData: NewRoomData = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
