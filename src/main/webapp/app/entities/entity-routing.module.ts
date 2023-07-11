import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'intervention',
        data: { pageTitle: 'Interventions' },
        loadChildren: () => import('./intervention/intervention.module').then(m => m.InterventionModule),
      },
      {
        path: 'floor-data',
        data: { pageTitle: 'FloorData' },
        loadChildren: () => import('./floor-data/floor-data.module').then(m => m.FloorDataModule),
      },
      {
        path: 'tier-data',
        data: { pageTitle: 'TierData' },
        loadChildren: () => import('./tier-data/tier-data.module').then(m => m.TierDataModule),
      },
      {
        path: 'room-data',
        data: { pageTitle: 'RoomData' },
        loadChildren: () => import('./room-data/room-data.module').then(m => m.RoomDataModule),
      },
      {
        path: 'floor',
        data: { pageTitle: 'Floors' },
        loadChildren: () => import('./floor/floor.module').then(m => m.FloorModule),
      },
      {
        path: 'tier',
        data: { pageTitle: 'Tiers' },
        loadChildren: () => import('./tier/tier.module').then(m => m.TierModule),
      },
      {
        path: 'room',
        data: { pageTitle: 'Rooms' },
        loadChildren: () => import('./room/room.module').then(m => m.RoomModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
