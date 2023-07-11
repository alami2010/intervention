import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TierComponent } from '../list/tier.component';
import { TierDetailComponent } from '../detail/tier-detail.component';
import { TierUpdateComponent } from '../update/tier-update.component';
import { TierRoutingResolveService } from './tier-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const tierRoute: Routes = [
  {
    path: '',
    component: TierComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TierDetailComponent,
    resolve: {
      tier: TierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TierUpdateComponent,
    resolve: {
      tier: TierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TierUpdateComponent,
    resolve: {
      tier: TierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tierRoute)],
  exports: [RouterModule],
})
export class TierRoutingModule {}
