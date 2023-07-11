import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TierComponent } from './list/tier.component';
import { TierDetailComponent } from './detail/tier-detail.component';
import { TierUpdateComponent } from './update/tier-update.component';
import { TierDeleteDialogComponent } from './delete/tier-delete-dialog.component';
import { TierRoutingModule } from './route/tier-routing.module';

@NgModule({
  imports: [SharedModule, TierRoutingModule],
  declarations: [TierComponent, TierDetailComponent, TierUpdateComponent, TierDeleteDialogComponent],
})
export class TierModule {}
