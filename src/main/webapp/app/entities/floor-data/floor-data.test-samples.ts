import { IFloorData, NewFloorData } from './floor-data.model';

export const sampleWithRequiredData: IFloorData = {
  id: 8897,
};

export const sampleWithPartialData: IFloorData = {
  id: 48955,
  name: 'incentivize Tennessee Granite',
};

export const sampleWithFullData: IFloorData = {
  id: 56154,
  name: 'Granite e-tailers Team-oriented',
};

export const sampleWithNewData: NewFloorData = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
