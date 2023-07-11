import { IFloor } from 'app/entities/floor/floor.model';
import { IRoom } from '../room/room.model';

export interface ITier {
  id: number;
  name?: string | null;
  floor?: Pick<IFloor, 'id'> | null;

  checked?: boolean | null;
  rooms?: IRoom[];
}

export type NewTier = Omit<ITier, 'id'> & { id: null };
