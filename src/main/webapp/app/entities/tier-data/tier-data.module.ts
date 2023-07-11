import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TierDataComponent } from './list/tier-data.component';
import { TierDataDetailComponent } from './detail/tier-data-detail.component';
import { TierDataUpdateComponent } from './update/tier-data-update.component';
import { TierDataDeleteDialogComponent } from './delete/tier-data-delete-dialog.component';
import { TierDataRoutingModule } from './route/tier-data-routing.module';

@NgModule({
  imports: [SharedModule, TierDataRoutingModule],
  declarations: [TierDataComponent, TierDataDetailComponent, TierDataUpdateComponent, TierDataDeleteDialogComponent],
})
export class TierDataModule {}
