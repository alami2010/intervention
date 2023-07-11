import { ITier } from 'app/entities/tier/tier.model';

export interface IRoom {
  id: number;
  name?: string | null;
  tier?: Pick<ITier, 'id'> | null;
  checked?: boolean | null;
}

export type NewRoom = Omit<IRoom, 'id'> & { id: null };
