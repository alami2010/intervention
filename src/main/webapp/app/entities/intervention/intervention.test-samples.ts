import dayjs from 'dayjs/esm';

import { TypeIntervention } from 'app/entities/enumerations/type-intervention.model';

import { IIntervention, NewIntervention } from './intervention.model';

export const sampleWithRequiredData: IIntervention = {
  id: 94912,
};

export const sampleWithPartialData: IIntervention = {
  id: 27159,
  type: TypeIntervention['Construction'],
  start: dayjs('2023-07-06T12:47'),
  finish: dayjs('2023-07-06T02:08'),
  unitNumber: 'azure',
  creationDate: dayjs('2023-07-06'),
};

export const sampleWithFullData: IIntervention = {
  id: 21866,
  type: TypeIntervention['Construction'],
  start: dayjs('2023-07-06T07:46'),
  finish: dayjs('2023-07-05T23:12'),
  raison: 'RSS Avon',
  unitNumber: 'panel synthesize Internal',
  creationDate: dayjs('2023-07-06'),
};

export const sampleWithNewData: NewIntervention = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
