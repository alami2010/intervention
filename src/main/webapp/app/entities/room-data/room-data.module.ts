import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RoomDataComponent } from './list/room-data.component';
import { RoomDataDetailComponent } from './detail/room-data-detail.component';
import { RoomDataUpdateComponent } from './update/room-data-update.component';
import { RoomDataDeleteDialogComponent } from './delete/room-data-delete-dialog.component';
import { RoomDataRoutingModule } from './route/room-data-routing.module';

@NgModule({
  imports: [SharedModule, RoomDataRoutingModule],
  declarations: [RoomDataComponent, RoomDataDetailComponent, RoomDataUpdateComponent, RoomDataDeleteDialogComponent],
})
export class RoomDataModule {}
