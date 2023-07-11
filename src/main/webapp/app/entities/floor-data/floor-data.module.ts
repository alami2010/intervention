import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FloorDataComponent } from './list/floor-data.component';
import { FloorDataDetailComponent } from './detail/floor-data-detail.component';
import { FloorDataUpdateComponent } from './update/floor-data-update.component';
import { FloorDataDeleteDialogComponent } from './delete/floor-data-delete-dialog.component';
import { FloorDataRoutingModule } from './route/floor-data-routing.module';

@NgModule({
  imports: [SharedModule, FloorDataRoutingModule],
  declarations: [FloorDataComponent, FloorDataDetailComponent, FloorDataUpdateComponent, FloorDataDeleteDialogComponent],
})
export class FloorDataModule {}
