import { IIntervention } from 'app/entities/intervention/intervention.model';
import { ITier } from '../tier/tier.model';

export interface IFloor {
  id: number;
  name?: string | null;
  intervention?: Pick<IIntervention, 'id'> | null;

  tiers?: ITier[] | [];
}

export type NewFloor = Omit<IFloor, 'id'> & { id: null };
