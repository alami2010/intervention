import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './intervention-delete-dialog.component.html',
})
export class InterventionDeleteDialogComponent {
  intervention?: IIntervention;

  constructor(protected interventionService: InterventionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.interventionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
