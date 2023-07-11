import { IFloor, NewFloor } from './floor.model';

export const sampleWithRequiredData: IFloor = {
  id: 27504,
};

export const sampleWithPartialData: IFloor = {
  id: 71771,
};

export const sampleWithFullData: IFloor = {
  id: 51374,
  name: 'Triple-buffered index green',
};

export const sampleWithNewData: NewFloor = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
