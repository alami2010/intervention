import { ITier, NewTier } from './tier.model';

export const sampleWithRequiredData: ITier = {
  id: 85725,
};

export const sampleWithPartialData: ITier = {
  id: 42134,
};

export const sampleWithFullData: ITier = {
  id: 61545,
  name: 'Awesome',
};

export const sampleWithNewData: NewTier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
