import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InterventionComponent } from './list/intervention.component';
import { InterventionDetailComponent } from './detail/intervention-detail.component';
import { InterventionUpdateComponent } from './update/intervention-update.component';
import { InterventionDeleteDialogComponent } from './delete/intervention-delete-dialog.component';
import { InterventionRoutingModule } from './route/intervention-routing.module';

@NgModule({
  imports: [SharedModule, InterventionRoutingModule],
  declarations: [InterventionComponent, InterventionDetailComponent, InterventionUpdateComponent, InterventionDeleteDialogComponent],
})
export class InterventionModule {}
