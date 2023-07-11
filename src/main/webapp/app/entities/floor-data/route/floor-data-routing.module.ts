import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FloorDataComponent } from '../list/floor-data.component';
import { FloorDataDetailComponent } from '../detail/floor-data-detail.component';
import { FloorDataUpdateComponent } from '../update/floor-data-update.component';
import { FloorDataRoutingResolveService } from './floor-data-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const floorDataRoute: Routes = [
  {
    path: '',
    component: FloorDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FloorDataDetailComponent,
    resolve: {
      floorData: FloorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FloorDataUpdateComponent,
    resolve: {
      floorData: FloorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FloorDataUpdateComponent,
    resolve: {
      floorData: FloorDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(floorDataRoute)],
  exports: [RouterModule],
})
export class FloorDataRoutingModule {}
