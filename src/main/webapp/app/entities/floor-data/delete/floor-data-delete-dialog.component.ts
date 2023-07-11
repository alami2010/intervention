import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFloorData } from '../floor-data.model';
import { FloorDataService } from '../service/floor-data.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './floor-data-delete-dialog.component.html',
})
export class FloorDataDeleteDialogComponent {
  floorData?: IFloorData;

  constructor(protected floorDataService: FloorDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.floorDataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
