import { ITierData, NewTierData } from './tier-data.model';

export const sampleWithRequiredData: ITierData = {
  id: 52031,
};

export const sampleWithPartialData: ITierData = {
  id: 60982,
};

export const sampleWithFullData: ITierData = {
  id: 88382,
  name: 'Granite',
};

export const sampleWithNewData: NewTierData = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
