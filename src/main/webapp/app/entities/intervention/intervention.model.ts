import dayjs from 'dayjs/esm';
import { TypeIntervention } from 'app/entities/enumerations/type-intervention.model';
import { IFloor } from '../floor/floor.model';

export interface IIntervention {
  id: number;
  type?: TypeIntervention | null;
  start?: dayjs.Dayjs | null;
  finish?: dayjs.Dayjs | null;
  raison?: string | null;

  email?: string | null;
  unitNumber?: string | null;
  creationDate?: dayjs.Dayjs | null;

  floors?: IFloor[] | [];
}

export type NewIntervention = Omit<IIntervention, 'id'> & { id: null };
