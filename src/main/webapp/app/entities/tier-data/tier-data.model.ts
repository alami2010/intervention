import { IFloorData } from 'app/entities/floor-data/floor-data.model';

export interface ITierData {
  id: number;
  name?: string | null;
  floor?: Pick<IFloorData, 'id'> | null;
}

export type NewTierData = Omit<ITierData, 'id'> & { id: null };
