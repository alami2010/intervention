import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TierDataComponent } from '../list/tier-data.component';
import { TierDataDetailComponent } from '../detail/tier-data-detail.component';
import { TierDataUpdateComponent } from '../update/tier-data-update.component';
import { TierDataRoutingResolveService } from './tier-data-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tierDataRoute: Routes = [
  {
    path: '',
    component: TierDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TierDataDetailComponent,
    resolve: {
      tierData: TierDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TierDataUpdateComponent,
    resolve: {
      tierData: TierDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TierDataUpdateComponent,
    resolve: {
      tierData: TierDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tierDataRoute)],
  exports: [RouterModule],
})
export class TierDataRoutingModule {}
