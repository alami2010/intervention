import { ITierData } from 'app/entities/tier-data/tier-data.model';

export interface IRoomData {
  id: number;
  name?: string | null;
  tier?: Pick<ITierData, 'id'> | null;
}

export type NewRoomData = Omit<IRoomData, 'id'> & { id: null };
