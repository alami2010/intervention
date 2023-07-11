export interface IFloorData {
  id: number;
  name?: string | null;
}

export type NewFloorData = Omit<IFloorData, 'id'> & { id: null };
