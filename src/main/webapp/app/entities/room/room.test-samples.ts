import { IRoom, NewRoom } from './room.model';

export const sampleWithRequiredData: IRoom = {
  id: 302,
};

export const sampleWithPartialData: IRoom = {
  id: 37668,
  name: 'Pizza Argentina bandwidth-monitored',
};

export const sampleWithFullData: IRoom = {
  id: 59376,
  name: 'Unit Bypass Agent',
};

export const sampleWithNewData: NewRoom = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
