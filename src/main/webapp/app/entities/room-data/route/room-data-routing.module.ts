import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RoomDataComponent } from '../list/room-data.component';
import { RoomDataDetailComponent } from '../detail/room-data-detail.component';
import { RoomDataUpdateComponent } from '../update/room-data-update.component';
import { RoomDataRoutingResolveService } from './room-data-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const roomDataRoute: Routes = [
  {
    path: '',
    component: RoomDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomDataDetailComponent,
    resolve: {
      roomData: RoomDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomDataUpdateComponent,
    resolve: {
      roomData: RoomDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomDataUpdateComponent,
    resolve: {
      roomData: RoomDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(roomDataRoute)],
  exports: [RouterModule],
})
export class RoomDataRoutingModule {}
